package com.example.rickandmorty.ui.locations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.rickandmorty.model.Character

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsFlowScreen(
    viewModel: LocationViewModel = remember { LocationViewModel() },
    onCharactersClick: () -> Unit = {},
    onEpisodesClick: () -> Unit = {},
    onResidentClick: ((Character) -> Unit)? = null
) {
    var selectedId by remember { mutableStateOf<Int?>(null) }

    if (selectedId == null) {
        val listState by viewModel.listState.collectAsState()
        LocationListScreen(
            locations = listState.ubicaciones,
            page = listState.pagina,
            totalPages = listState.info.pages,
            isLoading = listState.cargando,
            error = listState.error,
            appliedName = listState.nombreFiltro,
            appliedType = listState.tipoFiltro,
            appliedDimension = listState.dimensionFiltro,
            onApplyFilters = { name, type, dimension ->
                viewModel.cargarUbicaciones(pagina = 1, nombre = name, tipo = type, dimension = dimension)
            },
            onLocationClick = { id ->
                selectedId = id
                viewModel.cargarDetalleUbicacion(id)
            },
            onPreviousPage = {
                if (listState.pagina > 1) viewModel.cargarUbicaciones(pagina = listState.pagina - 1)
            },
            onNextPage = {
                if (listState.pagina < listState.info.pages) viewModel.cargarUbicaciones(pagina = listState.pagina + 1)
            },
            onRetry = { viewModel.cargarUbicaciones() },
            onCharactersClick = onCharactersClick,
            onEpisodesClick = onEpisodesClick
        )
    } else {
        val detailState by viewModel.detailState.collectAsState()
        val current = detailState.ubicacion

        if (current != null && !detailState.cargando) {
            LocationDetailScreen(
                location = current,
                residents = detailState.residentes,
                residentsLoading = detailState.residentesCargando,
                residentsError = detailState.residentesError,
                onBackClick = { selectedId = null },
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
                            IconButton(onClick = { selectedId = null }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = PrimaryText)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor)
                    )
                }
            ) { padding ->
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    if (detailState.cargando) {
                        CircularProgressIndicator(color = AccentColor)
                    } else {
                        ErrorMessage(
                            error = detailState.error ?: "No se encontró la ubicación.",
                            onRetry = { viewModel.cargarDetalleUbicacion(selectedId!!) }
                        )
                    }
                }
            }
        }
    }
}
