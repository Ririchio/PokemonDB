package ru.fefu.pokedex.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.fefu.pokedex.data.local.FavoritePokemonEntity
import ru.fefu.pokedex.data.repository.PokemonRepository
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeFavorites().collect { list ->
                _uiState.update { it.copy(items = list) }
            }
        }
    }

    fun removeFavorite(id: Int) {
        viewModelScope.launch {
            repository.removeFavorite(id)
        }
    }
}

data class FavoritesUiState(
    val items: List<FavoritePokemonEntity> = emptyList()
)