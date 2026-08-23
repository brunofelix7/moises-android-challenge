package dev.brunofelix.moiseschallenge.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.moiseschallenge.core.data.local.SongDatabase
import dev.brunofelix.moiseschallenge.core.data.local.dao.SongDao
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