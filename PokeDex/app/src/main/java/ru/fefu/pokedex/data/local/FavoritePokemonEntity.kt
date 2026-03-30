package ru.fefu.pokedex.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_pokemon")
data class FavoritePokemonEntity(
    @PrimaryKey val pokemonId: Int,
    val name: String,
    val imageUrl: String,
    val addedAt: Long
)