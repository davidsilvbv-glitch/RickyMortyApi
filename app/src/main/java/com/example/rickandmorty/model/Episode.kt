package com.example.rickandmorty.model

data class Episode(
    val id: Int,
    val name: String,
    val air_date: String,
    val episode: String,
    val characters: List<String> = emptyList(),
    val url: String = "",
    val created: String = ""
)
