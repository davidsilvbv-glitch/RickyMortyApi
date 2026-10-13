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
import com.example.rickandmorty.model.CharacterResponse
import com.example.rickandmorty.ui.character.CharacterDetailScreen
import com.example.rickandmorty.ui.character.CharacterListScreen
import com.example.rickandmorty.ui.episodes.EpisodeDetailScreen
import com.example.rickandmorty.ui.episodes.EpisodeListScreen
import com.example.rickandmorty.ui.episodes.MockEpisode
import com.example.rickandmorty.ui.locations.LocationsFlowScreen
import com.example.rickandmorty.ui.theme.RickAndMortyTheme
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private val AppBackground = Color(0xFF0F1412)
private val AppGreen = Color(0xFF97CE4C)

/** Servicio pequeño para mostrar personajes reales mientras integramos los módulos. */
private interface CharactersApi {
    @GET("character")
    fun obtenerPersonajes(): Call<CharacterResponse>
}

private val charactersApi: CharactersApi by lazy {
    Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CharactersApi::class.java)
}

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
    var selectedEpisode by remember { mutableStateOf<MockEpisode?>(null) }

    Box(Modifier.fillMaxSize().background(AppBackground)) {
        when (tab) {
            0 -> CharactersFlow(
                selectedCharacter = selectedCharacter,
                onCharacterClick = { selectedCharacter = it },
                onBackClick = { selectedCharacter = null }
            )
            1 -> LocationsFlowScreen(
                onCharactersClick = { tab = 0; selectedCharacter = null },
                onEpisodesClick = { tab = 2; selectedEpisode = null },
                onResidentClick = { character ->
                    selectedCharacter = character
                    tab = 0
                }
            )
            2 -> if (selectedEpisode == null) {
                EpisodeListScreen(onEpisodeClick = { selectedEpisode = it })
            } else {
                EpisodeDetailScreen(
                    episode = selectedEpisode!!,
                    onBackClick = { selectedEpisode = null }
                )
            }
        }

        // Cubre la barra decorativa de los módulos con una barra que sí navega.
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
    onBackClick: () -> Unit
) {
    var characters by remember { mutableStateOf<List<Character>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var retry by remember { mutableIntStateOf(0) }

    LaunchedEffect(retry) {
        loading = true
        error = null
        try {
            val response = withContext(Dispatchers.IO) { charactersApi.obtenerPersonajes().execute() }
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
            // El detalle de Adri no tiene barra inferior; reservar su espacio.
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
            onCharacterClick = onCharacterClick
        )
    }
}
