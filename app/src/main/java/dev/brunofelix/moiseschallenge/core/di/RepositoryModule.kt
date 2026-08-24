package dev.brunofelix.moiseschallenge.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.moiseschallenge.core.data.repository.AlbumRepositoryImpl
import dev.brunofelix.moiseschallenge.core.data.repository.SongRepositoryImpl
import dev.brunofelix.moiseschallenge.core.domain.repository.AlbumRepository
import dev.brunofelix.moiseschallenge.core.domain.repository.SongRepository
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