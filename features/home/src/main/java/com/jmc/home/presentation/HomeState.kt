package com.jmc.home.presentation

import com.jmc.home.data.model.Result

data class HomeState(
    val characters: List<Result> = emptyList(),
    val isLoading: Boolean = false,
    val isPaginating: Boolean = false,
    val query: String = "",
    val error: String? = null,
    val endReached: Boolean = false
)