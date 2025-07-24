package com.jmc.details.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmc.details.data.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject internal constructor(
    private val repository: DataRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf<DetailState>(DetailState.Loading)
        private set

    init {
        val id = savedStateHandle.get<Int>("characterId")
        if (id != null) {
            getCharacter(id)
        } else {
            state = DetailState.Error("Invalid character ID")
        }
    }


    private fun getCharacter(id: Int) {
        viewModelScope.launch {
            state = try {
                val character = repository.getDetailCharacter(id)
                DetailState.Success(character)
            } catch (e: Exception) {
                DetailState.Error(e.message ?: "Unknown error")
            }
        }
    }


}