package ru.fefu.pokedex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.fefu.pokedex.ui.components.PokemonItem
import ru.fefu.pokedex.ui.viewmodel.PokemonEvent
import ru.fefu.pokedex.ui.viewmodel.PokemonUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    uiState: PokemonUiState,
    onEvent: (PokemonEvent) -> Unit,
    onPokemonClick: (String) -> Unit,
    onBack: () -> Unit
) {

    val favoritePokemon = uiState.pokemonList.filter { pokemon ->
        uiState.favorites.contains(pokemon.id)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Избранное",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                favoritePokemon.isEmpty() -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = "No favorites",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )

                            Text(
                                "Нет избранных покемонов",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                "Нажмите на сердечко в списке,\nчтобы добавить покемона в избранное",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }

                else -> {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(favoritePokemon) { pokemon ->
                            PokemonItem(
                                pokemon = pokemon,
                                isFavorite = true,
                                onClick = { onPokemonClick(pokemon.name) },
                                onFavoriteClick = {
                                    onEvent(PokemonEvent.ToggleFavorite(pokemon.id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}