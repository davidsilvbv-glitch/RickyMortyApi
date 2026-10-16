package com.example.rickandmorty.data.remote.dto

data class PokemonDto(
    val id: Int,
    val name: String,
    val base_experience: Int,
    val height: Int,
    val weight: Int
)