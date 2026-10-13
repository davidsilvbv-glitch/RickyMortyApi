package com.example.rickandmorty.model.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EpisodeListScreen(
    episodes: List<MockEpisode> = com.example.rickandmorty.ui.episodes.EpisodeMockData.sampleEpisodes,
    onEpisodeClick: (MockEpisode) -> Unit = {}
) {
    com.example.rickandmorty.ui.episodes.EpisodeListScreen(
        episodes = episodes,
        onEpisodeClick = onEpisodeClick
    )
}

@Preview(showBackground = true)
@Composable
fun EpisodeListScreenPreview() {
    com.example.rickandmorty.ui.theme.RickAndMortyTheme {
        EpisodeListScreen()
    }
}
