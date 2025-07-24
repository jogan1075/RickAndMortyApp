package com.jmc.home.data

import com.jmc.home.data.model.CharacterResult
import com.jmc.home.data.model.Info
import com.jmc.home.data.model.Location
import com.jmc.home.data.model.Origin
import com.jmc.home.data.model.Result
import com.jmc.home.data.repository.Remote
import com.jmc.home.data.source.Factory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DataRepositoryTest {

    private lateinit var remote: Remote
    private lateinit var factory: Factory
    private lateinit var repository: DataRepository

    @Before
    fun setUp() {
        remote = mockk()
        factory = mockk()
        repository = DataRepository(factory)

        coEvery { factory.getRemote() } returns remote
    }

    @Test
    fun `given valid page when getAllCharacters is called then return CharacterResult`() = runTest {
        // Given
        val expectedResult = CharacterResult(
            info = Info(
                count = 1,
                pages = 1,
                next = "",
                prev = ""
            ),
            results = listOf(
                Result(
                    id = 1,
                    name = "Rick Sanchez",
                    status = "Alive",
                    species = "Human",
                    gender = "Male",
                    origin = Origin(name = "Earth (C-137)", url = ""),
                    location = Location(name = "Citadel of Ricks", url = ""),
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                    episode = emptyList(),
                    url = "https://rickandmortyapi.com/api/character/1",
                    created = "2017-11-04T18:48:46.250Z",
                    type = ""
                )
            )
        )

        coEvery { remote.getCharacters(1, null) } returns expectedResult

        // When
        val result = repository.getAllCharacters(1)

        // Then
        assertEquals(expectedResult, result)
        coVerify { remote.getCharacters(1, null) }
        coVerify { factory.getRemote() }
    }

    @Test
    fun `given query when getAllCharacters is called then return filtered result`() = runTest {
        // Given
        val expectedResult = CharacterResult(
            info = Info(count = 1, pages = 1, next = "", prev = ""),
            results = listOf(
                Result(
                    id = 1,
                    name = "Morty Smith",
                    status = "Alive",
                    species = "Human",
                    gender = "Male",
                    origin = Origin("Earth", ""),
                    location = Location("Earth", ""),
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                    episode = emptyList(),
                    url = "",
                    created = "",
                    type = ""
                )
            )
        )

        coEvery { factory.getRemote() } returns remote
        coEvery { remote.getCharacters(1, "Morty") } returns expectedResult

        // When
        val result = repository.getAllCharacters(1, "Morty")

        // Then
        assertEquals(expectedResult, result)
        coVerify { remote.getCharacters(1, "Morty") }
    }

    @Test
    fun `given page greater than 1 when getAllCharacters is called then return paged result`() = runTest {
        // Given
        val expectedResult = CharacterResult(
            info = Info(count = 2, pages = 2, next = "", prev = "1"),
            results = listOf(
                Result(
                    id = 2,
                    name = "Summer Smith",
                    status = "Alive",
                    species = "Human",
                    gender = "Female",
                    origin = Origin("Earth", ""),
                    location = Location("Earth", ""),
                    image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                    episode = emptyList(),
                    url = "",
                    created = "",
                    type = ""
                )
            )
        )

        coEvery { factory.getRemote() } returns remote
        coEvery { remote.getCharacters(2, null) } returns expectedResult

        // When
        val result = repository.getAllCharacters(2)

        // Then
        assertEquals(expectedResult, result)
        coVerify { remote.getCharacters(2, null) }
    }

    @Test(expected = RuntimeException::class)
    fun `given remote throws exception when getAllCharacters is called then exception is propagated`() = runTest {
        // Given
        val exception = RuntimeException("Network error")

        coEvery { factory.getRemote() } returns remote
        coEvery { remote.getCharacters(1, null) } throws exception

        // When
        repository.getAllCharacters(1)

        // Then -> exception is expected (handled by @Test annotation)
    }

    @Test(expected = RuntimeException::class)
    fun `given query when remote throws exception then exception is propagated`() = runTest {
        // Given
        val exception = RuntimeException("No characters found")
        coEvery { factory.getRemote() } returns remote
        coEvery { remote.getCharacters(1, "Unknown") } throws exception

        // When
        repository.getAllCharacters(1, "Unknown")

        // Then -> exception is expected
    }
}