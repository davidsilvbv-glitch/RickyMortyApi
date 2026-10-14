package com.example.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.network.RetrofitClient
import com.example.rickandmorty.ui.character.CharacterDetailScreen
import com.example.rickandmorty.ui.character.CharacterListScreen
import com.example.rickandmorty.ui.episodes.EpisodeDetailScreen
import com.example.rickandmorty.ui.episodes.EpisodeListScreen
import com.example.rickandmorty.ui.locations.LocationsFlowScreen
import com.example.rickandmorty.ui.theme.RickAndMortyTheme
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val AppBackground = Color(0xFF0F1412)
private val AppGreen = Color(0xFF97CE4C)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {
                RickAndMortyApp()
            }
        }
    }
}

@Composable
fun RickAndMortyApp() {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    var selectedCharacter by remember { mutableStateOf<Character?>(null) }
    var selectedEpisode by remember { mutableStateOf<Episode?>(null) }

    Box(Modifier.fillMaxSize().background(AppBackground)) {
        when (tab) {
            0 -> CharactersFlow(
                selectedCharacter = selectedCharacter,
                onCharacterClick = { selectedCharacter = it },
                onBackClick = { selectedCharacter = null },
                onCharactersClick = { tab = 0; selectedCharacter = null },
                onLocationsClick = { tab = 1 },
                onEpisodesClick = { tab = 2; selectedEpisode = null }
            )
            1 -> LocationsFlowScreen(
                onCharactersClick = { tab = 0; selectedCharacter = null },
                onEpisodesClick = { tab = 2; selectedEpisode = null },
                onResidentClick = { character ->
                    selectedCharacter = character
                    tab = 0
                }
            )
            2 -> EpisodesFlow(
                selectedEpisode = selectedEpisode,
                onEpisodeClick = { selectedEpisode = it },
                onBackClick = { selectedEpisode = null },
                onCharactersClick = { tab = 0; selectedCharacter = null },
                onLocationsClick = { tab = 1 },
                onEpisodesClick = { tab = 2; selectedEpisode = null },
                onCharacterClick = { character ->
                    selectedCharacter = character
                    tab = 0
                }
            )
        }

        // Navegación inferior persistente
        NavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            containerColor = AppBackground
        ) {
            NavigationBarItem(
                selected = tab == 0,
                onClick = { tab = 0; selectedCharacter = null },
                icon = { Icon(Icons.Default.Face, contentDescription = "Personajes") },
                label = { Text("Characters") },
                colors = appNavigationColors()
            )
            NavigationBarItem(
                selected = tab == 1,
                onClick = { tab = 1 },
                icon = { Icon(Icons.Default.Place, contentDescription = "Ubicaciones") },
                label = { Text("Locations") },
                colors = appNavigationColors()
            )
            NavigationBarItem(
                selected = tab == 2,
                onClick = { tab = 2; selectedEpisode = null },
                icon = { Icon(Icons.Default.Movie, contentDescription = "Episodios") },
                label = { Text("Episodes") },
                colors = appNavigationColors()
            )
        }
    }
}

@Composable
private fun appNavigationColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = AppGreen,
    selectedTextColor = AppGreen,
    unselectedIconColor = Color.LightGray,
    unselectedTextColor = Color.LightGray,
    indicatorColor = Color(0xFF203128)
)

@Composable
private fun CharactersFlow(
    selectedCharacter: Character?,
    onCharacterClick: (Character) -> Unit,
    onBackClick: () -> Unit,
    onCharactersClick: () -> Unit,
    onLocationsClick: () -> Unit,
    onEpisodesClick: () -> Unit
) {
    var characters by remember { mutableStateOf<List<Character>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var retry by remember { mutableIntStateOf(0) }

    LaunchedEffect(retry) {
        loading = true
        error = null
        try {
            val response = withContext(Dispatchers.IO) { RetrofitClient.api.getCharacters() }
            if (response.isSuccessful) {
                characters = response.body()?.results.orEmpty()
            } else {
                error = "No se pudieron cargar los personajes (HTTP ${response.code()})"
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            error = "No se pudo conectar con la API: ${e.message ?: "revisa Internet"}"
        }
        loading = false
    }

    when {
        selectedCharacter != null -> {
            Box(Modifier.fillMaxSize().padding(bottom = 80.dp)) {
                CharacterDetailScreen(character = selectedCharacter, onBackClick = onBackClick)
            }
        }
        loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = AppGreen)
        }
        error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = { retry++ }) { Text("${error} · Reintentar") }
        }
        else -> CharacterListScreen(
            characters = characters,
            onCharacterClick = onCharacterClick,
            onCharactersClick = onCharactersClick,
            onLocationsClick = onLocationsClick,
            onEpisodesClick = onEpisodesClick
        )
    }
}

@Composable
private fun EpisodesFlow(
    selectedEpisode: Episode?,
    onEpisodeClick: (Episode) -> Unit,
    onBackClick: () -> Unit,
    onCharactersClick: () -> Unit,
    onLocationsClick: () -> Unit,
    onEpisodesClick: () -> Unit,
    onCharacterClick: (Character) -> Unit
) {
    var episodes by remember { mutableStateOf<List<Episode>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var retry by remember { mutableIntStateOf(0) }

    var episodeCharacters by remember { mutableStateOf<List<Character>>(emptyList()) }
    var charactersLoading by remember { mutableStateOf(false) }
    var charactersError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(retry) {
        loading = true
        error = null
        try {
            val response = withContext(Dispatchers.IO) { RetrofitClient.api.getEpisodes() }
            if (response.isSuccessful) {
                episodes = response.body()?.results.orEmpty()
            } else {
                error = "No se pudieron cargar los episodios (HTTP ${response.code()})"
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            error = "Error de conexión: ${e.message ?: "revisa Internet"}"
        }
        loading = false
    }

    LaunchedEffect(selectedEpisode) {
        if (selectedEpisode != null) {
            charactersLoading = true
            charactersError = null
            episodeCharacters = emptyList()

            val ids = selectedEpisode.characters
                .mapNotNull { url -> url.substringAfterLast("/").toIntOrNull() }
                .distinct()

            if (ids.isNotEmpty()) {
                try {
                    val response = withContext(Dispatchers.IO) {
                        RetrofitClient.api.getCharactersByIds(ids.joinToString(","))
                    }
                    if (response.isSuccessful && response.body() != null) {
                        val json = response.body()!!
                        val gson = Gson()
                        val list = mutableListOf<Character>()
                        if (json.isJsonArray) {
                            json.asJsonArray.forEach { item ->
                                list.add(gson.fromJson(item, Character::class.java))
                            }
                        } else if (json.isJsonObject) {
                            list.add(gson.fromJson(json, Character::class.java))
                        }
                        episodeCharacters = list
                    } else {
                        charactersError = "No se pudieron cargar los personajes del episodio"
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    charactersError = "Error al cargar personajes: ${e.message}"
                }
            }
            charactersLoading = false
        }
    }

    if (selectedEpisode == null) {
        EpisodeListScreen(
            episodes = episodes,
            isLoading = loading,
            error = error,
            onRetry = { retry++ },
            onEpisodeClick = onEpisodeClick,
            onCharactersClick = onCharactersClick,
            onLocationsClick = onLocationsClick,
            onEpisodesClick = onEpisodesClick
        )
    } else {
        EpisodeDetailScreen(
            episode = selectedEpisode,
            characters = episodeCharacters,
            charactersLoading = charactersLoading,
            charactersError = charactersError,
            onBackClick = onBackClick,
            onCharactersClick = onCharactersClick,
            onLocationsClick = onLocationsClick,
            onEpisodesClick = onEpisodesClick,
            onCharacterClick = onCharacterClick
        )
    }
}
