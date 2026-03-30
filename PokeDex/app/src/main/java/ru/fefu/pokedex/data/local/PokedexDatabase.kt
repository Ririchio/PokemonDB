package ru.fefu.pokedex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoritePokemonEntity::class],
    version = 2,
    exportSchema = false
)
abstract class PokedexDatabase : RoomDatabase() {
    abstract fun favoritePokemonDao(): FavoritePokemonDao
}