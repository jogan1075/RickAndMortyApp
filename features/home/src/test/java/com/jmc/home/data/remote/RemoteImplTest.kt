package com.jmc.home.data.remote

import com.jmc.home.data.model.CharacterResult
import com.jmc.home.data.model.Info
import com.jmc.home.data.model.Location
import com.jmc.home.data.model.Origin
import com.jmc.home.data.model.Result
import com.jmc.home.data.remote.service.HomeApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteImplTest {

    private val api: HomeApi = mockk()
    private val remote = RemoteImpl(api)

    @Test
    fun `given valid page and query when getCharacters is called then return CharacterResult`() = runTest {

        val page = 1
        val query = "Rick"
        val expectedResult = CharacterResult(
            info = Info(count = 1, pages = 1, next = "", prev = ""),
            results = listOf(
                Result(
                    id = 1,
                    name = "Rick Sanchez",
                    status = "Alive",
                    species = "Human",
                    gender = "Male",
                    origin = Origin(name = "Earth (C-137)", url = "https://rickandmortyapi.com/api/location/1"),
                    location = Location(name = "Citadel of Ricks", url = "https://rickandmortyapi.com/api/location/3"),
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                    episode = listOf(
                        "https://rickandmortyapi.com/api/episode/1",
                        "https://rickandmortyapi.com/api/episode/2",
                        "https://rickandmortyapi.com/api/episode/3"
                    ),
                    url = "https://rickandmortyapi.com/api/character/1",
                    created = "2017-11-04T18:48:46.250Z",
                    type = ""
                )
            )
        )

        coEvery { api.getCharacters(page, query) } returns expectedResult

        val result = remote.getCharacters(page, query)

        assertEquals(expectedResult, result)
        coVerify(exactly = 1) { api.getCharacters(page, query) }
    }

    @Test(expected = RuntimeException::class)
    fun `given api throws exception when getCharacters is called then exception is propagated`() = runTest {

        val page = 1
        val query = null
        coEvery { api.getCharacters(page, query) } throws RuntimeException("Network error")

        remote.getCharacters(page, query)

    }
}