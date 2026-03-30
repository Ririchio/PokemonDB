package ru.fefu.pokedex.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.fefu.pokedex.data.model.PokemonDetail
import ru.fefu.pokedex.data.repository.PokemonRepository

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val pokemon = MutableStateFlow<PokemonDetail?>(null)
    private val isLoading = MutableStateFlow(false)
    private val error = MutableStateFlow<String?>(null)

    private var loadJob: Job? = null

    val uiState: StateFlow<PokemonDetailUiState> = combine(
        pokemon,
        repository.observeFavoriteIds(),
        isLoading,
        error
    ) { pokemon, favoriteIds, isLoading, error ->
        PokemonDetailUiState(
            isLoading = isLoading,
            pokemon = pokemon,
            isFavorite = pokemon?.id in favoriteIds,
            error = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PokemonDetailUiState()
    )

    fun load(idOrName: String) {
        if (idOrName.isBlank()) return

        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            isLoading.value = true
            error.value = null
            pokemon.value = null

            runCatching { repository.fetchPokemonDetail(idOrName) }
                .onSuccess { loadedPokemon ->
                    pokemon.value = loadedPokemon
                }
                .onFailure { throwable ->
                    error.value = throwable.message ?: "Failed to load details"
                }

            isLoading.value = false
        }
    }

    fun toggleFavorite() {
        val currentPokemon = uiState.value.pokemon ?: return

        viewModelScope.launch {
            if (uiState.value.isFavorite) {
                repository.removeFavorite(currentPokemon.id)
            } else {
                repository.addFavorite(
                    id = currentPokemon.id,
                    name = currentPokemon.name,
                    imageUrl = currentPokemon.imageUrl
                )
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