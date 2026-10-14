package com.example.rickandmorty.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PageInfoDto(
    @SerializedName("count") val count: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("prev") val prev: String?
)
