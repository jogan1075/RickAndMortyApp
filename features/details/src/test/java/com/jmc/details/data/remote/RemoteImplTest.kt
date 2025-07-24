package com.jmc.details.data.remote

import com.jmc.details.data.model.DetailResult
import com.jmc.details.data.model.Location
import com.jmc.details.data.model.Origin
import com.jmc.details.data.remote.services.DetailApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import kotlin.test.assertFailsWith

class RemoteImplTest {

    private val api = mockk<DetailApi>()
    private val remoteImpl = RemoteImpl(api)


    @Test
    fun `given valid id when getCharacterInfo is called then returns character data`() = runTest {
        // Given
        val characterId = 1
        val expectedDetail = DetailResult(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            gender = "Male",
            origin = Origin(name = "Earth", url = "https://rickandmortyapi.com/api/location/1"),
            location = Location(name = "Citadel of Ricks", url = "https://rickandmortyapi.com/api/location/3"),
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            created = "2017-11-04T18:48:46.250Z",
            episode = listOf(
                "https://rickandmortyapi.com/api/episode/1",
                "https://rickandmortyapi.com/api/episode/2"
            ),
            type = "",
            url = "https://rickandmortyapi.com/api/character/1"
        )
        coEvery { api.getCharacterInfo(characterId) } returns expectedDetail

        // When
        val result = remoteImpl.getCharacterInfo(characterId)

        // Then
        assertEquals(expectedDetail, result)
        coVerify(exactly = 1) { api.getCharacterInfo(characterId) }
    }


    @Test(expected = RuntimeException::class)
    fun `given api throws exception when getCharacterInfo is called then exception is propagated`() = runTest {
        // Given
        val characterId = 999
        coEvery { api.getCharacterInfo(characterId) } throws RuntimeException("Character not found")

        // When
        remoteImpl.getCharacterInfo(characterId)

        // Then (exception is expected)
    }

    @Test
    fun `exception is propagated`() = runTest {
        val characterId = 999
        coEvery { api.getCharacterInfo(characterId) } throws RuntimeException("Character not found")

        val exception = assertFailsWith<RuntimeException> {
            remoteImpl.getCharacterInfo(characterId)
        }

        assertEquals("Character not found", exception.message)
    }
}