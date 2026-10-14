package com.example.rickandmorty.ui.episodes

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
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.ui.theme.RickAndMortyTheme

private val ScreenBackground = Color(0xFF0F1412)
private val CardBackground = Color(0xFF1B221F)
private val AccentGreen = Color(0xFF97CE4C)
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFF8E9B90)
private val DarkText = Color(0xFF0F1412)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeListScreen(
    episodes: List<Episode> = emptyList(),
    isLoading: Boolean = false,
    error: String? = null,
    onRetry: () -> Unit = {},
    onEpisodeClick: (Episode) -> Unit = {},
    onCharactersClick: () -> Unit = {},
    onLocationsClick: () -> Unit = {},
    onEpisodesClick: () -> Unit = {}
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
            RickAndMortyBottomBar(
                selectedTab = 2,
                onCharactersClick = onCharactersClick,
                onLocationsClick = onLocationsClick,
                onEpisodesClick = onEpisodesClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        color = AccentGreen,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = error, color = TextPrimary)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onRetry,
                            colors = ButtonDefaults.buttonColors(containerColor = AccentGreen, contentColor = DarkText)
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        items(episodes, key = { it.id }) { episode ->
                            EpisodeItemCard(
                                episode = episode,
                                onClick = { onEpisodeClick(episode) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EpisodeItemCard(
    episode: Episode,
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
            Surface(
                shape = CircleShape,
                color = AccentGreen
            ) {
                Text(
                    text = episode.episode,
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
                    text = "Air ${episode.air_date}",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun RickAndMortyBottomBar(
    selectedTab: Int,
    onCharactersClick: () -> Unit = {},
    onLocationsClick: () -> Unit = {},
    onEpisodesClick: () -> Unit = {}
) {
    NavigationBar(
        containerColor = ScreenBackground,
        contentColor = TextSecondary
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = onCharactersClick,
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
            onClick = onLocationsClick,
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
            onClick = onEpisodesClick,
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
    RickAndMortyTheme {
        EpisodeListScreen(
            episodes = listOf(
                Episode(1, "Pilot", "Jan 27, 2022", "S01E01", emptyList(), "", ""),
                Episode(2, "Lawnmower Dog", "Jul 16, 2021", "S01E02", emptyList(), "", "")
            )
        )
    }
}
