package com.jmc.home.data.remote

import com.jmc.home.data.model.CharacterResult
import com.jmc.home.data.remote.service.HomeApi
import com.jmc.home.data.repository.Remote
import javax.inject.Inject

class RemoteImpl @Inject constructor(private val api: HomeApi) : Remote {

    override suspend fun getCharacters(page: Int, query: String?): CharacterResult {
        return api.getCharacters(page,query)
    }
}