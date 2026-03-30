package ru.fefu.pokedex.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.fefu.pokedex.data.api.PokeApi
import ru.fefu.pokedex.data.local.FavoritePokemonDao
import ru.fefu.pokedex.data.local.FavoritePokemonEntity
import ru.fefu.pokedex.data.model.FavoritePokemonItem
import ru.fefu.pokedex.data.model.PokemonDetail
import ru.fefu.pokedex.data.model.PokemonListItem
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val api: PokeApi,
    private val favoriteDao: FavoritePokemonDao
) {
    suspend fun fetchPokemonList(limit: Int = 50, offset: Int = 0): List<PokemonListItem> {
        return api.getPokemonList(limit = limit, offset = offset).results
    }

    suspend fun fetchPokemonDetail(idOrName: String): PokemonDetail {
        return api.getPokemonDetail(idOrName)
    }

    fun observeFavoriteIds(): Flow<Set<Int>> {
        return favoriteDao.observeFavoriteIds()
            .map { it.toSet() }
            .distinctUntilChanged()
    }

    fun observeFavorites(): Flow<List<FavoritePokemonItem>> {
        return favoriteDao.observeFavorites()
            .map { favorites -> favorites.map(FavoritePokemonEntity::toFavoritePokemonItem) }
    }

    suspend fun addFavorite(id: Int, name: String, imageUrl: String) {
        favoriteDao.insert(
            FavoritePokemonEntity(
                pokemonId = id,
                name = name,
                imageUrl = imageUrl,
                addedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun removeFavorite(id: Int) {
        favoriteDao.deleteById(id)
    }
}

private fun FavoritePokemonEntity.toFavoritePokemonItem(): FavoritePokemonItem {
    return FavoritePokemonItem(
        id = pokemonId,
        name = name,
        imageUrl = imageUrl
    )
}