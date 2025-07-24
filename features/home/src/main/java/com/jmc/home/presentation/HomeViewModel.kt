package com.jmc.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmc.home.data.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    private var currentPage = 1
    private var isLoadingNextPage = false

    private val searchQuery = MutableStateFlow("")

    init {
        observeSearchQuery()
        getCharacters()
    }

    fun onEvents(event: HomeScreenEvents) {
        when (event) {
            is HomeScreenEvents.LoadNextPage -> getCharacters()
            is HomeScreenEvents.OnSearchQueryChange -> {
                state = state.copy(query = event.query)
                searchQuery.value = event.query
            }
            is HomeScreenEvents.OnCharacterClick -> {}
        }
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQuery
                .debounce(1000)
                .distinctUntilChanged()
                .collect { query ->
                    resetPaginationAndSearch()
                }
        }
    }

    private fun resetPaginationAndSearch() {
        currentPage = 1
        state = state.copy(
            characters = emptyList(),
            endReached = false,
            error = null
        )
        getCharacters()
    }

    private fun getCharacters() {
        if (isLoadingNextPage || state.endReached) return

        viewModelScope.launch {
            isLoadingNextPage = true
            state = state.copy(
                isLoading = currentPage == 1,
                isPaginating = currentPage > 1,
                error = null
            )

            try {
                val result = repository.getAllCharacters(currentPage, state.query)
                val newCharacters = result.results
                val isLastPage = result.info.next == null

                state = state.copy(
                    characters = state.characters + newCharacters,
                    isLoading = false,
                    isPaginating = false,
                    endReached = isLastPage
                )

                currentPage++

            } catch (e: Exception) {
                state = state.copy(
                    isLoading = false,
                    isPaginating = false,
                    error = e.message ?: "Unknown error"
                )
            }

            isLoadingNextPage = false
        }
    }
}
