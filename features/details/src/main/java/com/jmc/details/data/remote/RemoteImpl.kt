package com.jmc.details.data.remote

import com.jmc.details.data.model.DetailResult
import com.jmc.details.data.remote.services.DetailApi
import com.jmc.details.data.repository.Remote
import javax.inject.Inject

class RemoteImpl @Inject constructor(private val api: DetailApi) : Remote{
    override suspend fun getCharacterInfo(id: Int): DetailResult {
        return api.getCharacterInfo(id)
    }
}