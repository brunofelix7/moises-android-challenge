package dev.brunofelix.moiseschallenge.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.moiseschallenge.local.source.RoomLocalDataSourceImpl
import dev.brunofelix.moiseschallenge.local.source.SongLocalDataSource
import dev.brunofelix.moiseschallenge.remote.source.ITunesRemoteDataSourceImpl
import dev.brunofelix.moiseschallenge.remote.source.SongRemoteDataSource
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