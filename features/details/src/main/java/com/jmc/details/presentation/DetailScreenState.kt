package com.jmc.details.presentation

import com.jmc.details.data.model.DetailResult

sealed class DetailState {
    object Loading : DetailState()
    data class Success(val character: DetailResult) : DetailState()
    data class Error(val message: String) : DetailState()
}