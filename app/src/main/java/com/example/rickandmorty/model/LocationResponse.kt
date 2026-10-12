package com.example.rickandmorty.model

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("info")
    val info: PageInfo,
    @SerializedName("results")
    val results: List<Location>
)
