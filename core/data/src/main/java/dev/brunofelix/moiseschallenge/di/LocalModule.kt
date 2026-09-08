package dev.brunofelix.moiseschallenge.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.moiseschallenge.local.SongDatabase
import dev.brunofelix.moiseschallenge.local.dao.SongDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideSongDatabase(
        @ApplicationContext context: Context
    ): SongDatabase {
        return Room.databaseBuilder(
            context,
            SongDatabase::class.java,
            SongDatabase.NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideSongDao(db: SongDatabase): SongDao {
        return db.songDao()
    }
}