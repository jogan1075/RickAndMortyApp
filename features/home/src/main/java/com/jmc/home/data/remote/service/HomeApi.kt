package com.jmc.home.data.remote.service

import com.jmc.home.data.model.CharacterResult
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApi {

    @GET("character/")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("name") name: String? = null
    ): CharacterResult
}