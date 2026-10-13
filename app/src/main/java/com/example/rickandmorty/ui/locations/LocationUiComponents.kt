package com.example.rickandmorty.ui.locations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal val BackgroundColor = Color(0xFF0F1412)
internal val CardColor = Color(0xFF1B221F)
internal val AccentColor = Color(0xFF97CE4C)
internal val PrimaryText = Color(0xFFFFFFFF)
internal val SecondaryText = Color(0xFF9CAAA0)

/** Las pestañas avisan a la pantalla principal; no cambian una variable local sin navegar. */
@Composable
internal fun LocationsBottomBar(
    onCharactersClick: () -> Unit,
    onLocationsClick: () -> Unit,
    onEpisodesClick: () -> Unit
) {
    NavigationBar(containerColor = BackgroundColor) {
        val tabColors = NavigationBarItemDefaults.colors(
            selectedIconColor = AccentColor,
            selectedTextColor = AccentColor,
            unselectedIconColor = SecondaryText,
            unselectedTextColor = SecondaryText,
            indicatorColor = CardColor
        )
        NavigationBarItem(
            selected = false,
            onClick = onCharactersClick,
            icon = { Icon(Icons.Default.Person, contentDescription = "Characters") },
            label = { Text("Characters") },
            colors = tabColors
        )
        NavigationBarItem(
            selected = true,
            onClick = onLocationsClick,
            icon = { Icon(Icons.Default.Place, contentDescription = "Locations") },
            label = { Text("Locations") },
            colors = tabColors
        )
        NavigationBarItem(
            selected = false,
            onClick = onEpisodesClick,
            icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Episodes") },
            label = { Text("Episodes") },
            colors = tabColors
        )
    }
}

/** La API de ubicaciones NO proporciona imágenes de los lugares. */
internal fun locationEmoji(type: String): String = when {
    type.contains("space", ignoreCase = true) -> "🛸"
    type.contains("planet", ignoreCase = true) -> "🪐"
    type.contains("microverse", ignoreCase = true) -> "🌌"
    type.contains("dimension", ignoreCase = true) -> "🌠"
    type.contains("dream", ignoreCase = true) -> "🌙"
    else -> "🌍"
}

@Composable
internal fun LocationSymbol(type: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.clip(CircleShape).background(Color(0xFF2B4138)),
        contentAlignment = Alignment.Center
    ) {
        Text(locationEmoji(type), fontSize = 29.sp)
    }
}

internal fun displayValue(value: String): String = value.ifBlank { "Unknown" }
