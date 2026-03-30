package ru.fefu.pokedex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.fefu.pokedex.ui.screens.FavoritesScreen
import ru.fefu.pokedex.ui.screens.PokemonDetailScreen
import ru.fefu.pokedex.ui.screens.PokemonListScreen

object Routes {
    const val POKEMON_LIST = "pokemon_list"
    const val POKEMON_DETAIL = "pokemon_detail/{pokemonId}"
    const val FAVORITES = "favorites"

    fun detail(pokemonId: String): String = "pokemon_detail/$pokemonId"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.POKEMON_LIST
    ) {
        composable(Routes.POKEMON_LIST) {
            PokemonListScreen(
                onPokemonClick = { id -> navController.navigate(Routes.detail(id)) },
                onFavoritesClick = { navController.navigate(Routes.FAVORITES) }
            )
        }

        composable(
            route = Routes.POKEMON_DETAIL,
            arguments = listOf(navArgument("pokemonId") { type = NavType.StringType })
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getString("pokemonId").orEmpty()
            PokemonDetailScreen(
                pokemonId = pokemonId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.FAVORITES) {
            FavoritesScreen(
                onPokemonClick = { id -> navController.navigate(Routes.detail(id)) },
                onBack = { navController.popBackStack() }
            )
        }
    }
}