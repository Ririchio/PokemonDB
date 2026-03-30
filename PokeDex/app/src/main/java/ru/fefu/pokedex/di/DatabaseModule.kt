package ru.fefu.pokedex.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.fefu.pokedex.data.local.FavoritePokemonDao
import ru.fefu.pokedex.data.local.PokedexDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PokedexDatabase {
        return Room.databaseBuilder(
            context,
            PokedexDatabase::class.java,
            "pokedex.db"
        ).build()
    }

    @Provides
    fun provideFavoritePokemonDao(db: PokedexDatabase): FavoritePokemonDao {
        return db.favoritePokemonDao()
    }
}