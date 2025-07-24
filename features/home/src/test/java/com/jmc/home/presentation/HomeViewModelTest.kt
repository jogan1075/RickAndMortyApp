package com.jmc.home.presentation

import com.jmc.home.data.DataRepository
import com.jmc.home.data.model.CharacterResult
import com.jmc.home.data.model.Info
import com.jmc.home.data.model.Location
import com.jmc.home.data.model.Origin
import com.jmc.home.data.model.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val repository: DataRepository = mockk()
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given repository returns characters when initialized then state is success`() = runTest {

        val characters = listOf(
            Result(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                gender = "Male",
                origin = Origin("Earth", ""),
                location = Location("Earth", ""),
                image = "",
                episode = emptyList(),
                url = "",
                created = "",
                type = ""
            )
        )
        val result = CharacterResult(
            info = Info(count = 1, pages = 1, next = "", prev = ""),
            results = characters
        )

        coEvery { repository.getAllCharacters(1, "") } returns result

        viewModel = HomeViewModel(repository)
        advanceUntilIdle()

        assertFalse(viewModel.state.isLoading)
        assertEquals(characters, viewModel.state.characters)
        assertNull(viewModel.state.error)
    }

    @Test
    fun `given LoadNextPage event when called then next page characters loaded`() = runTest {

        val firstPageCharacters = listOf(
            Result(id = 1, name = "Rick Sanchez", status = "Alive", species = "Human", gender = "Male",
                origin = Origin("Earth", ""), location = Location("Earth", ""),
                image = "", episode = emptyList(), url = "", created = "", type = "")
        )
        val secondPageCharacters = listOf(
            Result(id = 2, name = "Morty Smith", status = "Alive", species = "Human", gender = "Male",
                origin = Origin("Earth", ""), location = Location("Earth", ""),
                image = "", episode = emptyList(), url = "", created = "", type = "")
        )

        val firstResult = CharacterResult(
            info = Info(count = 2, pages = 2, next = "page2", prev = ""),
            results = firstPageCharacters
        )
        val secondResult = CharacterResult(
            info = Info(count = 2, pages = 2, next = "", prev = "page1"),
            results = secondPageCharacters
        )

        coEvery { repository.getAllCharacters(1, "") } returns firstResult
        coEvery { repository.getAllCharacters(2, "") } returns secondResult

        viewModel = HomeViewModel(repository)
        advanceUntilIdle()

        viewModel.onEvents(HomeScreenEvents.LoadNextPage)
        advanceUntilIdle()

        assertFalse(viewModel.state.isLoading)
        assertFalse(viewModel.state.isPaginating)
        assertEquals(firstPageCharacters + secondPageCharacters, viewModel.state.characters)
    }


    @Test
    fun `given OnSearchQueryChange event when query changes then search resets pagination and loads results`() = runTest {

        val query = "Morty"
        val searchedCharacters = listOf(
            Result(id = 3, name = "Morty Smith", status = "Alive", species = "Human", gender = "Male",
                origin = Origin("Earth", ""), location = Location("Earth", ""),
                image = "", episode = emptyList(), url = "", created = "", type = "")
        )
        val searchedResult = CharacterResult(Info(1, pages = 1, next = "", prev = ""), searchedCharacters)

        coEvery { repository.getAllCharacters(1, query) } returns searchedResult

        viewModel = HomeViewModel(repository)
        advanceUntilIdle()

        viewModel.onEvents(HomeScreenEvents.OnSearchQueryChange(query))
        advanceTimeBy(1000)
        advanceUntilIdle()

        assertFalse(viewModel.state.isLoading)
        assertEquals(searchedCharacters, viewModel.state.characters)
        assertFalse(viewModel.state.endReached)
        assertEquals(query, viewModel.state.query)
    }

    @Test
    fun `given repository throws exception when getCharacters then error state is set`() = runTest {

        val exception = RuntimeException("Network error")
        coEvery { repository.getAllCharacters(any(), any()) } throws exception

        viewModel = HomeViewModel(repository)
        advanceUntilIdle()

        assertFalse(viewModel.state.isLoading)
        assertFalse(viewModel.state.isPaginating)
        assertEquals("Network error", viewModel.state.error)
    }


}