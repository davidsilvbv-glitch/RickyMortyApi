package com.example.rickandmorty.ui.locations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.Location
import com.example.rickandmorty.model.LocationRef
import com.example.rickandmorty.model.OriginRef

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailScreen(
    location: Location,
    residents: List<Character>,
    residentsLoading: Boolean,
    residentsError: String?,
    onBackClick: () -> Unit,
    onCharactersClick: () -> Unit = {},
    onEpisodesClick: () -> Unit = {},
    onResidentClick: ((Character) -> Unit)? = null
) {
    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            TopAppBar(
                title = { Text("Location", color = PrimaryText, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = PrimaryText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor)
            )
        },
        bottomBar = {
            LocationsBottomBar(
                onCharactersClick = onCharactersClick,
                onLocationsClick = onBackClick,
                onEpisodesClick = onEpisodesClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(205.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF36534A), Color(0xFF162720), Color(0xFF0C1814))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // La API no proporciona fotografías de ubicaciones: ilustración decorativa.
                    Text(locationEmoji(location.type), fontSize = 86.sp)
                }
            }
            item {
                Text(location.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryText)
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoTile("TYPE", displayValue(location.type), Modifier.weight(1f))
                    InfoTile("DIMENSION", displayValue(location.dimension), Modifier.weight(1f))
                }
            }
            item {
                Column {
                    Text("RESIDENTS", fontWeight = FontWeight.Bold, color = PrimaryText, letterSpacing = 1.sp)
                    Spacer(Modifier.height(5.dp))
                    Text("${location.residents.size} registrados en la API", color = SecondaryText, fontSize = 13.sp)
                }
            }
            if (residentsLoading) {
                item {
                    Box(Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = AccentColor)
                    }
                }
            } else {
                if (residentsError != null) {
                    item { Text(residentsError, color = Color(0xFFFFB4AB)) }
                }
                if (residents.isEmpty() && residentsError == null) {
                    item { Text("No hay residentes registrados.", color = SecondaryText) }
                }
                items(residents.chunked(3)) { group ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        group.forEach { character ->
                            ResidentAvatar(
                                character,
                                onClick = onResidentClick?.let { click -> { click(character) } },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        repeat(3 - group.size) { Spacer(modifier = Modifier.weight(1f)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoTile(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, color = CardColor, shape = RoundedCornerShape(12.dp)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text(label, color = SecondaryText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(value, color = PrimaryText, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun ResidentAvatar(character: Character, onClick: (() -> Unit)?, modifier: Modifier = Modifier) {
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Column(
        modifier = modifier.then(clickModifier).padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier.size(75.dp).clip(CircleShape).background(CardColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = AccentColor)
            AsyncImage(
                model = character.image,
                contentDescription = "Foto de ${character.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().clip(CircleShape)
            )
        }
        Text(
            character.name,
            color = PrimaryText,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = 15.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationDetailPreview() {
    val residents = listOf(
        Character(1, "Rick Sanchez", "Alive", "Human", "", "Male", OriginRef("", ""), LocationRef("", ""), "https://rickandmortyapi.com/api/character/avatar/1.jpeg", emptyList(), "", ""),
        Character(2, "Morty Smith", "Alive", "Human", "", "Male", OriginRef("", ""), LocationRef("", ""), "https://rickandmortyapi.com/api/character/avatar/2.jpeg", emptyList(), "", "")
    )
    LocationDetailScreen(
        location = Location(3, "Citadel of Ricks", "Space station", "unknown", listOf("1", "2"), "", ""),
        residents = residents,
        residentsLoading = false,
        residentsError = null,
        onBackClick = {}
    )
}
