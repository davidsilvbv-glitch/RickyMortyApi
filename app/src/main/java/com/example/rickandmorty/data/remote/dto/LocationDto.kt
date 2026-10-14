package com.example.rickandmorty.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LocationDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String?,
    @SerializedName("dimension") val dimension: String?,
    @SerializedName("residents") val residents: List<String>?,
    @SerializedName("url") val url: String?,
    @SerializedName("created") val created: String?
)

data class LocationResponseDto(
    @SerializedName("info") val info: PageInfoDto?,
    @SerializedName("results") val results: List<LocationDto>?
)
