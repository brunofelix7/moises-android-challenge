package dev.brunofelix.moiseschallenge.feature.player.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.moiseschallenge.feature.player.domain.use_case.GetSavedSongByIdUseCase
import dev.brunofelix.moiseschallenge.feature.player.domain.use_case.GetSavedSongByIdUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class PlayerModule {

    @Binds
    abstract fun bindGetSavedSongByIdUseCase(
        impl: GetSavedSongByIdUseCaseImpl
    ): GetSavedSongByIdUseCase
}