package ru.fefu.pokedex.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.fefu.pokedex.data.model.PokemonDetail
import ru.fefu.pokedex.data.repository.PokemonRepository
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonDetailUiState())
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    private var lastFavoriteIds: Set<Int> = emptySet()

    init {
        viewModelScope.launch {
            repository.observeFavoriteIds().collect { ids ->
                lastFavoriteIds = ids
                val p = _uiState.value.pokemon
                _uiState.update { it.copy(isFavorite = p?.let { ids.contains(it.id) } ?: false) }
            }
        }
    }

    fun load(idOrName: String) {
        _uiState.update { PokemonDetailUiState(isLoading = true) }
        viewModelScope.launch {
            runCatching { repository.fetchPokemonDetail(idOrName) }
                .onSuccess { pokemon ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            pokemon = pokemon,
                            error = null,
                            isFavorite = lastFavoriteIds.contains(pokemon.id)
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Failed to load details") }
                }
        }
    }

    fun toggleFavorite() {
        val pokemon = _uiState.value.pokemon ?: return
        viewModelScope.launch {
            if (_uiState.value.isFavorite) {
                repository.removeFavorite(pokemon.id)
            } else {
                repository.addFavorite(pokemon.id, pokemon.name, pokemon.imageUrl)
            }
        }
    }
}

data class PokemonDetailUiState(
    val isLoading: Boolean = false,
    val pokemon: PokemonDetail? = null,
    val isFavorite: Boolean = false,
    val error: String? = null
)