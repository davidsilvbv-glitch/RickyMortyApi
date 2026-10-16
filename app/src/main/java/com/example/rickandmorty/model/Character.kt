package com.example.rickandmorty.model

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: OriginRef,
    val location: LocationRef,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String,
    // CAMPOS DE POKEMON PARA EL OBJETO FUSIONADO
    val pokemonNombre: String? = null,
    val pokemonExp: Int? = null
)