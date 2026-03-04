package ru.fefu.pokedex.data.repository

import ru.fefu.pokedex.data.api.ApiResult
import ru.fefu.pokedex.data.api.PokeApi
import ru.fefu.pokedex.data.local.FavoritePokemonDao
import ru.fefu.pokedex.data.local.FavoritePokemonEntity
import ru.fefu.pokedex.data.model.PokemonDetail
import ru.fefu.pokedex.data.model.PokemonListItem
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val api: PokeApi,
    private val favoriteDao: FavoritePokemonDao
) {
    suspend fun getPokemonList(): ApiResult<List<PokemonListItem>> {
        return try {
            val response = api.getPokemonList(limit = 50)
            ApiResult.Success(response.results)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Network error")
        }
    }

    suspend fun getPokemonDetail(id: String): ApiResult<PokemonDetail> {
        return try {
            val response = api.getPokemonDetail(id)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Failed to load details")
        }
    }

    suspend fun getFavoriteIds(): Set<Int> {
        return favoriteDao.getAllIds().toSet()
    }

    suspend fun addFavorite(id: Int) {
        favoriteDao.insert(FavoritePokemonEntity(pokemonId = id, addedAt = System.currentTimeMillis()))
    }

    suspend fun removeFavorite(id: Int) {
        favoriteDao.deleteById(id)
    }
}