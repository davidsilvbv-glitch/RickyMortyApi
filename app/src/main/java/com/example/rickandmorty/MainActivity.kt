package com.example.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.rickandmorty.ui.episodes.EpisodeDetailScreen
import com.example.rickandmorty.ui.episodes.EpisodeListScreen
import com.example.rickandmorty.ui.episodes.MockEpisode
import com.example.rickandmorty.ui.theme.RickAndMortyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {
                EpisodesFlowApp()
            }
        }
    }
}

@Composable
fun EpisodesFlowApp() {
    var selectedEpisode by remember { mutableStateOf<MockEpisode?>(null) }

    if (selectedEpisode == null) {
        EpisodeListScreen(
            onEpisodeClick = { episode ->
                selectedEpisode = episode
            }
        )
    } else {
        EpisodeDetailScreen(
            episode = selectedEpisode!!,
            onBackClick = {
                selectedEpisode = null
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodeAppPreview() {
    RickAndMortyTheme {
        EpisodesFlowApp()
    }
}
