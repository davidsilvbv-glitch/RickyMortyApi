package com.example.rickandmorty.model

import com.google.gson.annotations.SerializedName

data class LocationRef(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)
