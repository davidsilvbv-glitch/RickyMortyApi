package com.example.rickandmorty.network

import com.example.rickandmorty.model.Location
import com.example.rickandmorty.model.LocationResponse
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/** Endpoints usados por el flujo de Locations (delegando en RetrofitClient). */
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

    @GET("character/{ids}")
    fun getCharacters(@Path("ids") ids: String): Call<JsonElement>
}

object RickAndMortyLocationsClient {
    val api: RickAndMortyLocationsApi by lazy {
        RetrofitClient.api as? RickAndMortyLocationsApi
            ?: retrofit2.Retrofit.Builder()
                .baseUrl("https://rickandmortyapi.com/api/")
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build()
                .create(RickAndMortyLocationsApi::class.java)
    }
}
