package com.example.rickandmorty.data

import com.example.rickandmorty.data.remote.RetrofitClient
import com.example.rickandmorty.data.remote.RickAndMortyApiService
import com.example.rickandmorty.data.remote.dto.CharacterDto
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.model.Location
import com.example.rickandmorty.model.PageInfo
import com.example.rickandmorty.util.Resultado
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RickRepository(
    private val api: RickAndMortyApiService = RetrofitClient.api
) {

    suspend fun getCharacters(
        page: Int = 1,
        name: String? = null,
        status: String? = null,
        species: String? = null
    ): Resultado<Pair<PageInfo, List<Character>>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getCharacters(page, name, status, species)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val info = body.info?.toDomain() ?: PageInfo()
                val characters = body.results.orEmpty().map { it.toDomain() }
                Resultado.Exito(Pair(info, characters))
            } else if (response.code() == 404) {
                Resultado.Exito(Pair(PageInfo(), emptyList()))
            } else {
                Resultado.Error("No se pudieron obtener los personajes (HTTP ${response.code()})")
            }
        } catch (e: Exception) {
            Resultado.Error("Error de conexión. Verifica que tengas Internet.", e)
        }
    }

    suspend fun getCharactersByIds(ids: List<Int>): Resultado<List<Character>> = withContext(Dispatchers.IO) {
        if (ids.isEmpty()) return@withContext Resultado.Exito(emptyList())
        try {
            val response = api.getCharactersByIds(ids.joinToString(","))
            if (response.isSuccessful && response.body() != null) {
                val json = response.body()!!
                val gson = Gson()
                val list = mutableListOf<Character>()
                if (json.isJsonArray) {
                    json.asJsonArray.forEach { item ->
                        val dto = gson.fromJson(item, CharacterDto::class.java)
                        list.add(dto.toDomain())
                    }
                } else if (json.isJsonObject) {
                    val dto = gson.fromJson(json, CharacterDto::class.java)
                    list.add(dto.toDomain())
                }
                Resultado.Exito(list)
            } else {
                Resultado.Error("No se pudieron cargar los personajes por ID")
            }
        } catch (e: Exception) {
            Resultado.Error("Error de conexión al cargar personajes por ID", e)
        }
    }

    suspend fun getLocations(
        page: Int = 1,
        name: String? = null,
        type: String? = null,
        dimension: String? = null
    ): Resultado<Pair<PageInfo, List<Location>>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getLocations(page, name, type, dimension)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val info = body.info?.toDomain() ?: PageInfo()
                val locations = body.results.orEmpty().map { it.toDomain() }
                Resultado.Exito(Pair(info, locations))
            } else if (response.code() == 404) {
                Resultado.Exito(Pair(PageInfo(), emptyList()))
            } else {
                Resultado.Error("No se pudieron cargar las ubicaciones (HTTP ${response.code()})")
            }
        } catch (e: Exception) {
            Resultado.Error("Error de conexión. Verifica que tengas Internet.", e)
        }
    }

    suspend fun getLocationDetail(id: Int): Resultado<Location> = withContext(Dispatchers.IO) {
        try {
            val response = api.getLocation(id)
            if (response.isSuccessful && response.body() != null) {
                Resultado.Exito(response.body()!!.toDomain())
            } else {
                Resultado.Error("Ubicación no encontrada (HTTP ${response.code()})")
            }
        } catch (e: Exception) {
            Resultado.Error("Error de conexión al abrir la ubicación", e)
        }
    }

    suspend fun getEpisodes(
        page: Int = 1,
        name: String? = null,
        episode: String? = null
    ): Resultado<Pair<PageInfo, List<Episode>>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getEpisodes(page, name, episode)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val info = body.info?.toDomain() ?: PageInfo()
                val episodes = body.results.orEmpty().map { it.toDomain() }
                Resultado.Exito(Pair(info, episodes))
            } else if (response.code() == 404) {
                Resultado.Exito(Pair(PageInfo(), emptyList()))
            } else {
                Resultado.Error("No se pudieron cargar los episodios (HTTP ${response.code()})")
            }
        } catch (e: Exception) {
            Resultado.Error("Error de conexión. Verifica que tengas Internet.", e)
        }
    }
}
