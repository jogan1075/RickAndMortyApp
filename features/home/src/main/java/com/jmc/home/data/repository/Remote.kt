package com.jmc.home.data.repository

import com.jmc.home.data.model.CharacterResult

interface Remote {

    suspend fun getCharacters(page: Int, query: String?): CharacterResult
}