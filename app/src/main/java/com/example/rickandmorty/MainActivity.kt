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
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.ui.character.CharacterDetailScreen
import com.example.rickandmorty.ui.character.CharacterListScreen
import com.example.rickandmorty.ui.character.CharacterViewModel
import com.example.rickandmorty.ui.episodes.EpisodeDetailScreen
import com.example.rickandmorty.ui.episodes.EpisodeListScreen
import com.example.rickandmorty.ui.episodes.EpisodeViewModel
import com.example.rickandmorty.ui.locations.LocationsFlowScreen
import com.example.rickandmorty.ui.theme.RickAndMortyTheme

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
    viewModel: CharacterViewModel = viewModel(),
    selectedCharacter: Character?,
    onCharacterClick: (Character) -> Unit,
    onBackClick: () -> Unit,
    onCharactersClick: () -> Unit,
    onLocationsClick: () -> Unit,
    onEpisodesClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        selectedCharacter != null -> {
            Box(Modifier.fillMaxSize().padding(bottom = 80.dp)) {
                CharacterDetailScreen(character = selectedCharacter, onBackClick = onBackClick)
            }
        }
        uiState.cargando -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = AppGreen)
        }
        uiState.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = { viewModel.cargarPersonajes() }) {
                Text("${uiState.error} · Reintentar")
            }
        }
        else -> CharacterListScreen(
            characters = uiState.personajes,
            onCharacterClick = onCharacterClick,
            onCharactersClick = onCharactersClick,
            onLocationsClick = onLocationsClick,
            onEpisodesClick = onEpisodesClick
        )
    }
}

@Composable
private fun EpisodesFlow(
    viewModel: EpisodeViewModel = viewModel(),
    selectedEpisode: Episode?,
    onEpisodeClick: (Episode) -> Unit,
    onBackClick: () -> Unit,
    onCharactersClick: () -> Unit,
    onLocationsClick: () -> Unit,
    onEpisodesClick: () -> Unit,
    onCharacterClick: (Character) -> Unit
) {
    val listState by viewModel.listState.collectAsState()
    val detailState by viewModel.detailState.collectAsState()

    if (selectedEpisode == null) {
        EpisodeListScreen(
            episodes = listState.episodios,
            isLoading = listState.cargando,
            error = listState.error,
            onRetry = { viewModel.cargarEpisodios() },
            onEpisodeClick = { episode ->
                viewModel.seleccionarEpisodio(episode)
                onEpisodeClick(episode)
            },
            onCharactersClick = onCharactersClick,
            onLocationsClick = onLocationsClick,
            onEpisodesClick = onEpisodesClick
        )
    } else {
        EpisodeDetailScreen(
            episode = selectedEpisode,
            characters = detailState.personajes,
            charactersLoading = detailState.cargandoPersonajes,
            charactersError = detailState.errorPersonajes,
            onBackClick = {
                viewModel.seleccionarEpisodio(null)
                onBackClick()
            },
            onCharactersClick = onCharactersClick,
            onLocationsClick = onLocationsClick,
            onEpisodesClick = onEpisodesClick,
            onCharacterClick = onCharacterClick
        )
    }
}
