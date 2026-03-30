package ru.fefu.pokedex.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.fefu.pokedex.data.model.PokemonListItem
import ru.fefu.pokedex.data.repository.PokemonRepository
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonListUiState())
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeFavoriteIds().collect { ids ->
                _uiState.update { it.copy(favoriteIds = ids) }
            }
        }
        refresh()
    }

    fun refresh() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            runCatching { repository.fetchPokemonList(limit = 50) }
                .onSuccess { list ->
                    _uiState.update { it.copy(isLoading = false, pokemonList = list, error = null) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Network error") }
                }
        }
    }

    fun onSearchChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun clearSearch() {
        _uiState.update { it.copy(searchQuery = "") }
    }

    fun toggleFavorite(item: PokemonListItem) {
        viewModelScope.launch {
            val isFav = _uiState.value.favoriteIds.contains(item.id)
            if (isFav) {
                repository.removeFavorite(item.id)
            } else {
                repository.addFavorite(item.id, item.name, item.imageUrl)
            }
        }
    }
}

data class PokemonListUiState(
    val isLoading: Boolean = false,
    val pokemonList: List<PokemonListItem> = emptyList(),
    val favoriteIds: Set<Int> = emptySet(),
    val error: String? = null,
    val searchQuery: String = ""
) {
    val filteredPokemon: List<PokemonListItem>
        get() {
            val q = searchQuery.trim()
            if (q.isEmpty()) return pokemonList
            return pokemonList.filter {
                it.name.contains(q, ignoreCase = true) || it.id.toString().contains(q)
            }
        }

    val isEmpty: Boolean
        get() = !isLoading && error == null && filteredPokemon.isEmpty()
}