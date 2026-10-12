package com.example.rickandmorty.model.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Color definitions matching the dark Rick and Morty UI theme
private val ScreenBackground = Color(0xFF0F1412)
private val CardBackground = Color(0xFF1B221F)
private val AccentGreen = Color(0xFF97CE4C)
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFF8E9B90)
private val DarkText = Color(0xFF0F1412)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeListScreen(
    episodes: List<MockEpisode> = EpisodeMockData.sampleEpisodes,
    onEpisodeClick: (MockEpisode) -> Unit = {}
) {
    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Episodes",
                        color = TextPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = TextPrimary
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ScreenBackground
                )
            )
        },
        bottomBar = {
            RickAndMortyBottomBar(selectedTab = 2)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(episodes) { episode ->
                EpisodeItemCard(
                    episode = episode,
                    onClick = { onEpisodeClick(episode) }
                )
            }
        }
    }
}

@Composable
fun EpisodeItemCard(
    episode: MockEpisode,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = CardBackground,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Lime green badge with episode code (e.g. S01E01)
            Surface(
                shape = CircleShape,
                color = AccentGreen
            ) {
                Text(
                    text = episode.code,
                    color = DarkText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "- ${episode.name}",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Air ${episode.airDate}",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun RickAndMortyBottomBar(selectedTab: Int) {
    NavigationBar(
        containerColor = ScreenBackground,
        contentColor = TextSecondary
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = {},
            icon = { Icon(Icons.Default.Person, contentDescription = "Characters") },
            label = { Text("Characters") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentGreen,
                selectedTextColor = AccentGreen,
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = {},
            icon = { Icon(Icons.Default.Place, contentDescription = "Locations") },
            label = { Text("Locations") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentGreen,
                selectedTextColor = AccentGreen,
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = {},
            icon = { Icon(Icons.Default.Movie, contentDescription = "Episodes") },
            label = { Text("Episodes") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentGreen,
                selectedTextColor = AccentGreen,
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary,
                indicatorColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodeListScreenPreview() {
    EpisodeListScreen()
}
