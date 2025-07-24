package com.jmc.details.data.repository

import com.jmc.details.data.model.DetailResult
import retrofit2.http.Path

interface Remote {
    suspend fun getCharacterInfo(
        id: Int
    ): DetailResult
}