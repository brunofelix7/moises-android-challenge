package dev.brunofelix.moiseschallenge.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.moiseschallenge.core.data.remote.source.ITunesRemoteDataSourceImpl
import dev.brunofelix.moiseschallenge.core.data.remote.source.SongRemoteDataSource

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindSongRemoteDataSource(
        impl: ITunesRemoteDataSourceImpl
    ): SongRemoteDataSource
}