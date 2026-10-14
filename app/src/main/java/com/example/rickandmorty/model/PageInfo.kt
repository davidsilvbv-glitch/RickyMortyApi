package com.example.rickandmorty.model

data class PageInfo(
    val count: Int = 0,
    val pages: Int = 1,
    val next: String? = null,
    val prev: String? = null
)
