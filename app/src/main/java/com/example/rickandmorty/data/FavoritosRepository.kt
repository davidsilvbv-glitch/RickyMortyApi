package com.example.rickandmorty.data

import com.example.rickandmorty.data.local.FavoritoDao
import com.example.rickandmorty.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritosRepository(private val dao: FavoritoDao) {

    fun observarFavoritos(): Flow<List<Character>> =
        dao.observarFavoritos().map { entidades -> entidades.map { it.toCharacter() } }

    fun observarIds(): Flow<Set<Int>> =
        dao.observarIdsFavoritos().map { ids -> ids.toSet() }

    suspend fun agregar(character: Character) = dao.insertar(character.toFavoritoEntity())

    suspend fun quitar(id: Int) = dao.eliminarPorId(id)
}
