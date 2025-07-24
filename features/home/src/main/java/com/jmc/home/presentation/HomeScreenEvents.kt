package com.jmc.home.presentation

sealed class HomeScreenEvents {
    object LoadNextPage : HomeScreenEvents()
    data class OnCharacterClick(val id: Int) : HomeScreenEvents()
    data class OnSearchQueryChange(val query: String) : HomeScreenEvents()
}