package com.example.rickandmorty.network

import com.example.rickandmorty.model.Location
import com.example.rickandmorty.model.LocationResponse
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/** Endpoints reales usados por el flujo de Locations. */
interface RickAndMortyLocationsApi {
    @GET("location")
    fun getLocations(
        @Query("page") page: Int = 1,
        @Query("name") name: String? = null,
        @Query("type") type: String? = null,
        @Query("dimension") dimension: String? = null
    ): Call<LocationResponse>

    @GET("location/{id}")
    fun getLocation(@Path("id") id: Int): Call<Location>

    // La API devuelve un objeto si es un solo ID y un arreglo si son varios.
    @GET("character/{ids}")
    fun getCharacters(@Path("ids") ids: String): Call<JsonElement>
}

object RickAndMortyLocationsClient {
    val api: RickAndMortyLocationsApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyLocationsApi::class.java)
    }
}
