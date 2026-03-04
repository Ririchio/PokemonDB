package ru.fefu.pokedex.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fefu.pokedex.data.api.ApiResult
import ru.fefu.pokedex.data.model.PokemonDetail
import ru.fefu.pokedex.data.model.PokemonListItem
import ru.fefu.pokedex.data.repository.PokemonRepository
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonUiState())
    val uiState: StateFlow<PokemonUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
        loadPokemon()
    }

    fun onEvent(event: PokemonEvent) {
        when (event) {
            PokemonEvent.LoadPokemon -> loadPokemon()
            is PokemonEvent.SearchPokemon -> {
                _uiState.value = _uiState.value.copy(searchQuery = event.query)
            }
            PokemonEvent.ClearSearch -> {
                _uiState.value = _uiState.value.copy(searchQuery = "")
            }
            is PokemonEvent.ToggleFavorite -> toggleFavorite(event.pokemonId)
            is PokemonEvent.LoadPokemonDetail -> loadPokemonDetail(event.id)
        }
    }

    private fun loadPokemon() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            when (val result = repository.getPokemonList()) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        pokemonList = result.data,
                        error = null
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to load: ${result.message}"
                    )
                }
                else -> Unit
            }
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            val ids = repository.getFavoriteIds()
            _uiState.value = _uiState.value.copy(favorites = ids)
        }
    }

    private fun toggleFavorite(pokemonId: Int) {
        val current = _uiState.value.favorites.toMutableSet()
        val nowFavorite = !current.contains(pokemonId)

        if (nowFavorite) current.add(pokemonId) else current.remove(pokemonId)
        _uiState.value = _uiState.value.copy(favorites = current)

        viewModelScope.launch {
            if (nowFavorite) repository.addFavorite(pokemonId) else repository.removeFavorite(pokemonId)
        }
    }

    private fun loadPokemonDetail(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingDetail = true,
                errorDetail = null,
                selectedPokemon = null
            )

            when (val result = repository.getPokemonDetail(id)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoadingDetail = false,
                        selectedPokemon = result.data
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoadingDetail = false,
                        errorDetail = result.message
                    )
                }
                else -> Unit
            }
        }
    }
}

data class PokemonUiState(
    val isLoading: Boolean = false,
    val pokemonList: List<PokemonListItem> = emptyList(),
    val favorites: Set<Int> = emptySet(),
    val error: String? = null,
    val searchQuery: String = "",
    val isLoadingDetail: Boolean = false,
    val selectedPokemon: PokemonDetail? = null,
    val errorDetail: String? = null
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
        get() = !isLoading && filteredPokemon.isEmpty()
}

sealed class PokemonEvent {
    object LoadPokemon : PokemonEvent()
    data class SearchPokemon(val query: String) : PokemonEvent()
    data class ToggleFavorite(val pokemonId: Int) : PokemonEvent()
    data class LoadPokemonDetail(val id: String) : PokemonEvent()
    object ClearSearch : PokemonEvent()
}