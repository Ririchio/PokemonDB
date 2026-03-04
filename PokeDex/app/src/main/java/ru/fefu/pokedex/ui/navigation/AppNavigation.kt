package ru.fefu.pokedex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.fefu.pokedex.ui.screens.FavoritesScreen
import ru.fefu.pokedex.ui.screens.PokemonDetailScreen
import ru.fefu.pokedex.ui.screens.PokemonListScreen
import ru.fefu.pokedex.ui.viewmodel.PokemonEvent
import ru.fefu.pokedex.ui.viewmodel.PokemonViewModel

object Routes {
    const val POKEMON_LIST = "pokemon_list"
    const val POKEMON_DETAIL = "pokemon_detail/{pokemonId}"
    const val FAVORITES = "favorites"

    fun createDetailRoute(pokemonId: String): String {
        return "pokemon_detail/$pokemonId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: PokemonViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.POKEMON_LIST
    ) {
        composable(Routes.POKEMON_LIST) {
            PokemonListScreen(
                uiState = uiState,
                onEvent = { event -> viewModel.onEvent(event) },
                onPokemonClick = { pokemonName ->
                    navController.navigate(Routes.createDetailRoute(pokemonName))
                },
                onFavoritesClick = {
                    navController.navigate(Routes.FAVORITES)
                }
            )
        }

        composable(
            route = Routes.POKEMON_DETAIL,
            arguments = listOf(navArgument("pokemonId") { type = NavType.StringType })
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getString("pokemonId") ?: ""

            LaunchedEffect(pokemonId) {
                if (pokemonId.isNotEmpty()) {
                    viewModel.onEvent(PokemonEvent.LoadPokemonDetail(pokemonId))
                }
            }

            PokemonDetailScreen(
                uiState = uiState,
                onEvent = { event -> viewModel.onEvent(event) },
                onBack = { navController.popBackStack() },
                pokemonId = pokemonId
            )
        }

        composable(Routes.FAVORITES) {
            FavoritesScreen(
                uiState = uiState,
                onEvent = { event -> viewModel.onEvent(event) },
                onPokemonClick = { pokemonName ->
                    navController.navigate(Routes.createDetailRoute(pokemonName))
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}