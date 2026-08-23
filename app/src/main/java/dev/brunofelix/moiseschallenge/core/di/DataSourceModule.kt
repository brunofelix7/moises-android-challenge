package dev.brunofelix.moiseschallenge.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.moiseschallenge.core.data.local.source.RoomLocalDataSourceImpl
import dev.brunofelix.moiseschallenge.core.data.local.source.SongLocalDataSource
import dev.brunofelix.moiseschallenge.core.data.remote.source.ITunesRemoteDataSourceImpl
import dev.brunofelix.moiseschallenge.core.data.remote.source.SongRemoteDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindSongRemoteDataSource(
        impl: ITunesRemoteDataSourceImpl
    ): SongRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindSongLocalDataSource(
        impl: RoomLocalDataSourceImpl
    ): SongLocalDataSource
}