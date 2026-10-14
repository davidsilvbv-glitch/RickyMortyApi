package com.example.rickandmorty.model

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String = "",
    val gender: String,
    val origin: OriginRef = OriginRef(),
    val location: LocationRef = LocationRef(),
    val image: String,
    val episode: List<String> = emptyList(),
    val url: String = "",
    val created: String = ""
)
