package dev.brunofelix.moiseschallenge.feature.album.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.moiseschallenge.feature.album.domain.use_case.GetAlbumByIdUseCase
import dev.brunofelix.moiseschallenge.feature.album.domain.use_case.GetAlbumByIdUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class AlbumModule {

    @Binds
    abstract fun bindGetAlbumByIdUseCase(
        impl: GetAlbumByIdUseCaseImpl
    ): GetAlbumByIdUseCase
}