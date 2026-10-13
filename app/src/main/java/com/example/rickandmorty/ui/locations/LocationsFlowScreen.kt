package com.example.rickandmorty.ui.locations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.Location
import com.example.rickandmorty.model.PageInfo
import com.example.rickandmorty.network.RickAndMortyLocationsClient
import com.google.gson.Gson
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/** Entrada a tu módulo. Se invoca desde MainActivity o desde la navegación general. */
@Composable
fun LocationsFlowScreen(
    onCharactersClick: () -> Unit = {},
    onEpisodesClick: () -> Unit = {},
    onResidentClick: ((Character) -> Unit)? = null
) {
    var selectedId by remember { mutableStateOf<Int?>(null) }
    if (selectedId == null) {
        LocationListRoute(
            onLocationClick = { selectedId = it },
            onCharactersClick = onCharactersClick,
            onEpisodesClick = onEpisodesClick
        )
    } else {
        LocationDetailRoute(
            locationId = selectedId!!,
            onBackClick = { selectedId = null },
            onCharactersClick = onCharactersClick,
            onEpisodesClick = onEpisodesClick,
            onResidentClick = onResidentClick
        )
    }
}

@Composable
private fun LocationListRoute(
    onLocationClick: (Int) -> Unit,
    onCharactersClick: () -> Unit,
    onEpisodesClick: () -> Unit
) {
    var page by remember { mutableIntStateOf(1) }
    var appliedName by remember { mutableStateOf("") }
    var appliedType by remember { mutableStateOf("") }
    var appliedDimension by remember { mutableStateOf("") }
    var requestVersion by remember { mutableIntStateOf(0) }
    var locations by remember { mutableStateOf<List<Location>>(emptyList()) }
    var info by remember { mutableStateOf<PageInfo?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    DisposableEffect(page, appliedName, appliedType, appliedDimension, requestVersion) {
        var active = true
        loading = true
        error = null
        val call = RickAndMortyLocationsClient.api.getLocations(
            page = page,
            name = appliedName.ifBlank { null },
            type = appliedType.ifBlank { null },
            dimension = appliedDimension.ifBlank { null }
        )
        call.enqueue(object : Callback<com.example.rickandmorty.model.LocationResponse> {
            override fun onResponse(
                call: Call<com.example.rickandmorty.model.LocationResponse>,
                response: Response<com.example.rickandmorty.model.LocationResponse>
            ) {
                if (!active) return
                loading = false
                if (response.isSuccessful && response.body() != null) {
                    locations = response.body()!!.results
                    info = response.body()!!.info
                } else if (response.code() == 404) {
                    locations = emptyList()
                    info = null
                } else {
                    error = "No se pudieron cargar las ubicaciones (HTTP ${response.code()})."
                }
            }

            override fun onFailure(call: Call<com.example.rickandmorty.model.LocationResponse>, t: Throwable) {
                if (!active || call.isCanceled) return
                loading = false
                error = "Error de conexión. Verifica que tengas Internet."
            }
        })
        onDispose { active = false; call.cancel() }
    }

    LocationListScreen(
        locations = locations,
        page = page,
        totalPages = info?.pages ?: 1,
        isLoading = loading,
        error = error,
        appliedName = appliedName,
        appliedType = appliedType,
        appliedDimension = appliedDimension,
        onApplyFilters = { name, type, dimension ->
            // Cambiamos los tres filtros antes de disparar la siguiente consulta.
            appliedName = name
            appliedType = type
            appliedDimension = dimension
            page = 1
        },
        onLocationClick = onLocationClick,
        onPreviousPage = { if (page > 1) page-- },
        onNextPage = { if (page < (info?.pages ?: 1)) page++ },
        onRetry = { requestVersion++ },
        onCharactersClick = onCharactersClick,
        onEpisodesClick = onEpisodesClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationDetailRoute(
    locationId: Int,
    onBackClick: () -> Unit,
    onCharactersClick: () -> Unit,
    onEpisodesClick: () -> Unit,
    onResidentClick: ((Character) -> Unit)?
) {
    var location by remember(locationId) { mutableStateOf<Location?>(null) }
    var residents by remember(locationId) { mutableStateOf<List<Character>>(emptyList()) }
    var loading by remember(locationId) { mutableStateOf(true) }
    var residentsLoading by remember(locationId) { mutableStateOf(true) }
    var error by remember(locationId) { mutableStateOf<String?>(null) }
    var residentsError by remember(locationId) { mutableStateOf<String?>(null) }
    var requestVersion by remember { mutableIntStateOf(0) }

    DisposableEffect(locationId, requestVersion) {
        var active = true
        val calls = mutableListOf<Call<*>>()
        loading = true
        residentsLoading = true
        error = null
        residentsError = null
        residents = emptyList()

        val locationCall = RickAndMortyLocationsClient.api.getLocation(locationId)
        calls.add(locationCall)
        locationCall.enqueue(object : Callback<Location> {
            override fun onResponse(call: Call<Location>, response: Response<Location>) {
                if (!active) return
                loading = false
                val result = response.body()
                if (!response.isSuccessful || result == null) {
                    error = "No se encontró la ubicación (HTTP ${response.code()})."
                    residentsLoading = false
                    return
                }
                location = result
                val ids = result.residents
                    .mapNotNull { url -> url.substringAfterLast('/').toIntOrNull() }
                    .distinct()
                if (ids.isEmpty()) {
                    residentsLoading = false
                    return
                }

                // Evita una solicitud por residente: la API admite IDs separados por comas.
                val groups = ids.chunked(25)
                var remaining = groups.size
                var partialFailure = false
                val resolved = mutableMapOf<Int, Character>()
                val gson = Gson()
                groups.forEach { group ->
                    val charactersCall = RickAndMortyLocationsClient.api.getCharacters(group.joinToString(","))
                    calls.add(charactersCall)
                    charactersCall.enqueue(object : Callback<JsonElement> {
                        override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                            if (!active) return
                            val json = response.body()
                            if (response.isSuccessful && json != null) {
                                try {
                                    if (json.isJsonArray) {
                                        json.asJsonArray.forEach { item ->
                                            val character = gson.fromJson(item, Character::class.java)
                                            resolved[character.id] = character
                                        }
                                    } else if (json.isJsonObject) {
                                        val character = gson.fromJson(json, Character::class.java)
                                        resolved[character.id] = character
                                    }
                                } catch (e: RuntimeException) {
                                    partialFailure = true
                                }
                            } else {
                                partialFailure = true
                            }
                            remaining--
                            if (remaining == 0) {
                                residents = ids.mapNotNull { resolved[it] }
                                residentsLoading = false
                                if (partialFailure) residentsError = "Algunos residentes no pudieron cargarse."
                            }
                        }

                        override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                            if (!active || call.isCanceled) return
                            partialFailure = true
                            remaining--
                            if (remaining == 0) {
                                residents = ids.mapNotNull { resolved[it] }
                                residentsLoading = false
                                residentsError = "Algunos residentes no pudieron cargarse."
                            }
                        }
                    })
                }
            }

            override fun onFailure(call: Call<Location>, t: Throwable) {
                if (!active || call.isCanceled) return
                loading = false
                residentsLoading = false
                error = "Error de conexión. No se pudo abrir la ubicación."
            }
        })
        onDispose {
            active = false
            calls.forEach { it.cancel() }
        }
    }

    val current = location
    if (current != null && !loading) {
        LocationDetailScreen(
            location = current,
            residents = residents,
            residentsLoading = residentsLoading,
            residentsError = residentsError,
            onBackClick = onBackClick,
            onCharactersClick = onCharactersClick,
            onEpisodesClick = onEpisodesClick,
            onResidentClick = onResidentClick
        )
    } else {
        Scaffold(
            containerColor = BackgroundColor,
            topBar = {
                TopAppBar(
                    title = { Text("Location", color = PrimaryText) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = PrimaryText)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor)
                )
            }
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                if (loading) {
                    CircularProgressIndicator(color = AccentColor)
                } else {
                    ErrorMessage(error ?: "No se encontró la ubicación.", onRetry = { requestVersion++ })
                }
            }
        }
    }
}
