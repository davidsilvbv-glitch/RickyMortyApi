package com.example.rickandmorty.model

import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("info")
    val info: PageInfo,
    @SerializedName("results")
    val results: List<Character>
)
