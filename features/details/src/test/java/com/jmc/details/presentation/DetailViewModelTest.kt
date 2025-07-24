package com.jmc.details.presentation

import androidx.lifecycle.SavedStateHandle
import com.jmc.details.data.DataRepository
import com.jmc.details.data.model.DetailResult
import com.jmc.details.data.model.Location
import com.jmc.details.data.model.Origin
import com.jmc.details.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: DataRepository = mockk()
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: DetailViewModel

    @Test
    fun `given valid character id, when repository returns character, then state is Success`() = runTest {
        // Given
        val characterId = 1
        val expectedDetail = DetailResult(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            gender = "Male",
            origin = Origin("Earth", ""),
            location = Location("Citadel of Ricks", ""),
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            created = "",
            episode = emptyList(),
            type = "",
            url = ""
        )
        savedStateHandle = SavedStateHandle(mapOf("characterId" to characterId))

        coEvery { repository.getDetailCharacter(characterId) } returns expectedDetail

        // When
        viewModel = DetailViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Then
        val state = viewModel.state
        assertTrue(state is DetailState.Success)
        assertEquals(expectedDetail, (state as DetailState.Success).character)

        coVerify { repository.getDetailCharacter(characterId) }
    }

    @Test
    fun `given invalid character id in SavedStateHandle, then state is Error`() = runTest {
        // Given
        savedStateHandle = SavedStateHandle(emptyMap())

        // When
        viewModel = DetailViewModel(repository, savedStateHandle)

        // Then
        val state = viewModel.state
        assertTrue(state is DetailState.Error)
        assertEquals("Invalid character ID", (state as DetailState.Error).message)
    }

    @Test
    fun `given exception from repository, then state is Error`() = runTest {
        // Given
        val characterId = 999
        savedStateHandle = SavedStateHandle(mapOf("characterId" to characterId))

        coEvery { repository.getDetailCharacter(characterId) } throws RuntimeException("Character not found")

        // When
        viewModel = DetailViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Then
        val state = viewModel.state
        assertTrue(state is DetailState.Error)
        assertEquals("Character not found", (state as DetailState.Error).message)

        coVerify { repository.getDetailCharacter(characterId) }
    }
}

