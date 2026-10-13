package com.example.rickandmorty.ui.locations

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rickandmorty.model.Location

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationListScreen(
    locations: List<Location>,
    page: Int,
    totalPages: Int,
    isLoading: Boolean,
    error: String?,
    appliedName: String,
    appliedType: String,
    appliedDimension: String,
    onApplyFilters: (name: String, type: String, dimension: String) -> Unit,
    onLocationClick: (Int) -> Unit,
    onPreviousPage: () -> Unit,
    onNextPage: () -> Unit,
    onRetry: () -> Unit,
    onCharactersClick: () -> Unit = {},
    onEpisodesClick: () -> Unit = {}
) {
    var searchText by remember(appliedName) { mutableStateOf(appliedName) }
    var typeText by remember(appliedType) { mutableStateOf(appliedType) }
    var dimensionText by remember(appliedDimension) { mutableStateOf(appliedDimension) }
    var showFilters by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text("Locations", fontSize = 28.sp, color = PrimaryText, fontWeight = FontWeight.Bold)
                },
                actions = {
                    IconButton(onClick = { showFilters = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtrar", tint = PrimaryText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor)
            )
        },
        bottomBar = {
            LocationsBottomBar(
                onCharactersClick = onCharactersClick,
                onLocationsClick = {},
                onEpisodesClick = onEpisodesClick
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    label = { Text("Buscar ubicación") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        onApplyFilters(searchText.trim(), typeText.trim(), dimensionText.trim())
                    }),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = CardColor,
                        unfocusedContainerColor = CardColor,
                        focusedTextColor = PrimaryText,
                        unfocusedTextColor = PrimaryText,
                        focusedIndicatorColor = AccentColor,
                        unfocusedIndicatorColor = SecondaryText,
                        focusedLabelColor = AccentColor,
                        unfocusedLabelColor = SecondaryText
                    )
                )
                IconButton(onClick = {
                    onApplyFilters(searchText.trim(), typeText.trim(), dimensionText.trim())
                }) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar", tint = AccentColor)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (isLoading) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(60.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = AccentColor)
                        }
                    }
                } else if (error != null) {
                    item {
                        ErrorMessage(error = error, onRetry = onRetry)
                    }
                } else if (locations.isEmpty()) {
                    item {
                        Text(
                            "No encontramos ubicaciones con esos filtros.",
                            color = SecondaryText,
                            modifier = Modifier.padding(vertical = 28.dp)
                        )
                    }
                } else {
                    items(locations, key = { it.id }) { location ->
                        LocationListCard(location, onClick = { onLocationClick(location.id) })
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(onClick = onPreviousPage, enabled = page > 1) {
                                Text("Anterior")
                            }
                            Text("$page / $totalPages", color = SecondaryText)
                            Button(
                                onClick = onNextPage,
                                enabled = page < totalPages,
                                colors = ButtonDefaults.buttonColors(containerColor = AccentColor, contentColor = BackgroundColor)
                            ) {
                                Text("Siguiente")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showFilters) {
        AlertDialog(
            onDismissRequest = { showFilters = false },
            title = { Text("Filtrar ubicaciones") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Los filtros consultan directamente la API.")
                    OutlinedTextField(
                        value = typeText,
                        onValueChange = { typeText = it },
                        label = { Text("Tipo (ej. Planet)") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = dimensionText,
                        onValueChange = { dimensionText = it },
                        label = { Text("Dimensión") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showFilters = false
                    onApplyFilters(searchText.trim(), typeText.trim(), dimensionText.trim())
                }) { Text("Aplicar") }
            },
            dismissButton = {
                TextButton(onClick = {
                    typeText = ""
                    dimensionText = ""
                    showFilters = false
                    onApplyFilters("", "", "")
                }) { Text("Limpiar todo") }
            }
        )
    }
}

@Composable
private fun LocationListCard(location: Location, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = CardColor,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            LocationSymbol(type = location.type, modifier = Modifier.size(54.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(location.name, color = PrimaryText, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
                Spacer(Modifier.height(3.dp))
                Text(displayValue(location.type), color = SecondaryText, fontSize = 13.sp)
                Text("⌖ ${displayValue(location.dimension)}", color = SecondaryText, fontSize = 12.sp)
            }
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = AccentColor)
        }
    }
}

@Composable
internal fun ErrorMessage(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(error, color = PrimaryText)
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = AccentColor, contentColor = BackgroundColor)
        ) { Text("Reintentar") }
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationListPreview() {
    LocationListScreen(
        locations = listOf(
            Location(1, "Earth (C-137)", "Planet", "Dimension C-137", emptyList(), "", ""),
            Location(3, "Citadel of Ricks", "Space station", "unknown", emptyList(), "", ""),
            Location(5, "Anatomy Park", "Microverse", "Dimension C-137", emptyList(), "", "")
        ),
        page = 1,
        totalPages = 7,
        isLoading = false,
        error = null,
        appliedName = "",
        appliedType = "",
        appliedDimension = "",
        onApplyFilters = { _, _, _ -> },
        onLocationClick = {},
        onPreviousPage = {},
        onNextPage = {},
        onRetry = {}
    )
}
