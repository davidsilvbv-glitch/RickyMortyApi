package com.example.rickandmorty.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personaje_cache")
data class PersonajeCacheEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val status: String,
    val species: String,
    val image: String
)
