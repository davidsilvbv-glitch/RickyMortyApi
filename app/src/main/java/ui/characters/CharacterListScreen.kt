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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.LocationRef
import com.example.rickandmorty.model.OriginRef

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    characters: List<Character>,
    onCharacterClick: (Character) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Characters", fontWeight = FontWeight.Bold, color = Color.Green) },
                actions = {
                    IconButton(onClick = { /* Acción de búsqueda */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF121212))
            )
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
            items(characters) { character ->
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
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Placeholder para la imagen del personaje
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text("Img", color = Color.White)
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
                val statusColor = if (character.status.lowercase() == "alive") Color.Green else Color.Red
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
        Character(1, "Rick Sanchez", "Alive", "Human", "", "Male", OriginRef("", ""), LocationRef("", ""), "", emptyList(), "", ""),
        Character(2, "Morty Smith", "Alive", "Human", "", "Male", OriginRef("", ""), LocationRef("", ""), "", emptyList(), "", "")
    )
    CharacterListScreen(characters = mockCharacters, onCharacterClick = {})
}