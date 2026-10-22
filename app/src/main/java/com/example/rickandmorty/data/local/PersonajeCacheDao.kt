package com.example.rickandmorty.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonajeCacheDao {
    @Query("SELECT * FROM personaje_cache ORDER BY id ASC")
    fun observarTodos(): Flow<List<PersonajeCacheEntity>>

    @Query("SELECT COUNT(*) FROM personaje_cache")
    suspend fun contar(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(personajes: List<PersonajeCacheEntity>)
}
