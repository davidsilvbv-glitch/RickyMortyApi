package com.example.rickandmorty.model.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EpisodeDetailScreen(
    episode: MockEpisode = com.example.rickandmorty.ui.episodes.EpisodeMockData.sampleEpisodes.first(),
    onBackClick: () -> Unit = {}
) {
    com.example.rickandmorty.ui.episodes.EpisodeDetailScreen(
        episode = episode,
        onBackClick = onBackClick
    )
}

@Preview(showBackground = true)
@Composable
fun EpisodeDetailScreenPreview() {
    com.example.rickandmorty.ui.theme.RickAndMortyTheme {
        EpisodeDetailScreen()
    }
}
