package com.jmc.home.data

import com.jmc.home.data.model.CharacterResult
import com.jmc.home.data.source.Factory
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class DataRepository @Inject constructor(private val factory: Factory) {

    suspend fun getAllCharacters(page: Int,query: String? = null) :CharacterResult {
        return coroutineScope {
            val result = async { factory.getRemote().getCharacters(page,query) }.await()
            return@coroutineScope result
        }
    }
}