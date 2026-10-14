package com.example.rickandmorty.ui.character

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.LocationRef
import com.example.rickandmorty.model.OriginRef

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    characters: List<Character>,
    onCharacterClick: (Character) -> Unit,
    onCharactersClick: () -> Unit = {},
    onLocationsClick: () -> Unit = {},
    onEpisodesClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Characters", fontWeight = FontWeight.Bold, color = Color(0xFF97CE4C)) },
                actions = {
                    IconButton(onClick = { /* Acción de búsqueda */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF121212))
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1E1E1E),
                contentColor = Color.White
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = onCharactersClick,
                    icon = { Icon(Icons.Default.Person, contentDescription = "Characters") },
                    label = { Text("Characters") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF97CE4C),
                        selectedTextColor = Color(0xFF97CE4C),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color(0xFF121212)
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onLocationsClick,
                    icon = { Icon(Icons.Default.Place, contentDescription = "Locations") },
                    label = { Text("Locations") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF97CE4C),
                        selectedTextColor = Color(0xFF97CE4C),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color(0xFF121212)
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onEpisodesClick,
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Episodes") },
                    label = { Text("Episodes") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF97CE4C),
                        selectedTextColor = Color(0xFF97CE4C),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color(0xFF121212)
                    )
                )
            }
        },
        containerColor = Color(0xFF121212)
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(characters, key = { it.id }) { character ->
                CharacterCard(character = character, onClick = { onCharacterClick(character) })
            }
        }
    }
}

@Composable
fun CharacterCard(character: Character, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = character.image,
                    contentDescription = character.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = character.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White,
                maxLines = 1
            )

            Text(
                text = character.species,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val statusColor = when (character.status.lowercase()) {
                    "alive" -> Color(0xFF97CE4C)
                    "dead" -> Color.Red
                    else -> Color.Gray
                }
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = character.status, fontSize = 12.sp, color = Color.LightGray)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterListPreview() {
    val mockCharacters = listOf(
        Character(1, "Rick Sanchez", "Alive", "Human", "", "Male", OriginRef("", ""), LocationRef("", ""), "https://rickandmortyapi.com/api/character/avatar/1.jpeg", emptyList(), "", ""),
        Character(2, "Morty Smith", "Alive", "Human", "", "Male", OriginRef("", ""), LocationRef("", ""), "https://rickandmortyapi.com/api/character/avatar/2.jpeg", emptyList(), "", "")
    )
    CharacterListScreen(characters = mockCharacters, onCharacterClick = {})
}
