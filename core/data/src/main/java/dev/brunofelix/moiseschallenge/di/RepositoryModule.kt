package dev.brunofelix.moiseschallenge.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.moiseschallenge.repository.AlbumRepository
import dev.brunofelix.moiseschallenge.repository.SongRepository
import dev.brunofelix.moiseschallenge.repository.AlbumRepositoryImpl
import dev.brunofelix.moiseschallenge.repository.SongRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSongRepository(
        impl: SongRepositoryImpl
    ): SongRepository

    @Binds
    @Singleton
    abstract fun bindAlbumRepository(
        impl: AlbumRepositoryImpl
    ): AlbumRepository
}