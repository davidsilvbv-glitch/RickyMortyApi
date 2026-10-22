package com.example.rickandmorty.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritos")
data class FavoritoEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val imagenUrl: String,
    val agregadoEn: Long = System.currentTimeMillis()
)
