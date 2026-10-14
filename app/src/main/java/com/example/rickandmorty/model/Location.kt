package com.example.rickandmorty.model

data class Location(
    val id: Int,
    val name: String,
    val type: String = "Unknown",
    val dimension: String = "Unknown",
    val residents: List<String> = emptyList(),
    val url: String = "",
    val created: String = ""
)
