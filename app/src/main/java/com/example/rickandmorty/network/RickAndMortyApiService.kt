package com.example.rickandmorty.network

import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.CharacterResponse
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.model.EpisodeResponse
import com.example.rickandmorty.model.Location
import com.example.rickandmorty.model.LocationResponse
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz Retrofit principal para consumir la API de Rick and Morty.
 * Soporta Corrutinas (suspend) y Calls estándar de Retrofit.
 */
interface RickAndMortyApiService {

    // --- CHARACTERS ---
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int = 1,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null
    ): Response<CharacterResponse>

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): Response<Character>

    @GET("character/{ids}")
    suspend fun getCharactersByIds(@Path("ids") ids: String): Response<JsonElement>

    // --- LOCATIONS ---
    @GET("location")
    fun getLocations(
        @Query("page") page: Int = 1,
        @Query("name") name: String? = null,
        @Query("type") type: String? = null,
        @Query("dimension") dimension: String? = null
    ): Call<LocationResponse>

    @GET("location/{id}")
    fun getLocation(@Path("id") id: Int): Call<Location>

    @GET("character/{ids}")
    fun getCharactersCall(@Path("ids") ids: String): Call<JsonElement>

    // --- EPISODES ---
    @GET("episode")
    suspend fun getEpisodes(
        @Query("page") page: Int = 1,
        @Query("name") name: String? = null,
        @Query("episode") episode: String? = null
    ): Response<EpisodeResponse>

    @GET("episode/{id}")
    suspend fun getEpisode(@Path("id") id: Int): Response<Episode>
}

object RetrofitClient {
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    val api: RickAndMortyApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyApiService::class.java)
    }
}
