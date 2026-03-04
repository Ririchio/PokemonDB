package ru.fefu.pokedex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.fefu.pokedex.ui.components.ErrorState
import ru.fefu.pokedex.ui.components.LoadingState
import ru.fefu.pokedex.ui.components.PokemonItem
import ru.fefu.pokedex.ui.viewmodel.PokemonEvent
import ru.fefu.pokedex.ui.viewmodel.PokemonUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    uiState: PokemonUiState,
    onEvent: (PokemonEvent) -> Unit,
    onPokemonClick: (String) -> Unit,
    onFavoritesClick: () -> Unit
) {
    val searchQuery = uiState.searchQuery

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pokédex", style = MaterialTheme.typography.headlineSmall) },
                actions = {
                    IconButton(onClick = onFavoritesClick) {
                        Box {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = "Favorites",
                                tint = if (uiState.favorites.isNotEmpty())
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )

                            if (uiState.favorites.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = 4.dp, y = (-4).dp)
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                            }
                        }
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
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { onEvent(PokemonEvent.SearchPokemon(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search Pokémon by name or ID...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onEvent(PokemonEvent.ClearSearch) }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = MaterialTheme.shapes.large
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    uiState.isLoading -> {
                        LoadingState()
                    }

                    uiState.error != null -> {
                        ErrorState(
                            message = uiState.error,
                            onRetry = { onEvent(PokemonEvent.LoadPokemon) }
                        )
                    }

                    uiState.isEmpty -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("No Pokémon found", style = MaterialTheme.typography.bodyLarge)
                            if (uiState.searchQuery.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(onClick = { onEvent(PokemonEvent.ClearSearch) }) {
                                    Text("Clear search")
                                }
                            }
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.filteredPokemon) { pokemon ->
                                PokemonItem(
                                    pokemon = pokemon,
                                    isFavorite = uiState.favorites.contains(pokemon.id),
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
}