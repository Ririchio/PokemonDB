package ru.fefu.pokedex.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoritePokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavoritePokemonEntity)

    @Query("DELETE FROM favorite_pokemon WHERE pokemonId = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT pokemonId FROM favorite_pokemon")
    suspend fun getAllIds(): List<Int>
}