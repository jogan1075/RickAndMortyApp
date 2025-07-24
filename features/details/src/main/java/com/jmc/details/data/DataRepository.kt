package com.jmc.details.data

import com.jmc.details.data.model.DetailResult
import com.jmc.details.data.source.Factory
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal class DataRepository @Inject constructor(private val factory: Factory) {

    suspend fun getDetailCharacter(id: Int): DetailResult {
       return coroutineScope {
            async { factory.getRemote().getCharacterInfo(id) }.await()
        }

    }
}