package com.jmc.details.data.remote.services

import com.jmc.details.data.model.DetailResult
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailApi {

    @GET("character/{id}")
    suspend fun getCharacterInfo(
        @Path("id") id: Int
    ): DetailResult
}