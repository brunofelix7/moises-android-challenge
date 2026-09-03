package dev.brunofelix.moiseschallenge.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.moiseschallenge.core.domain.use_case.GetLastPlayedSongUseCase
import dev.brunofelix.moiseschallenge.core.domain.use_case.GetLastPlayedSongUseCaseImpl
import dev.brunofelix.moiseschallenge.core.domain.use_case.UpdateLastPlayedSongUseCase
import dev.brunofelix.moiseschallenge.core.domain.use_case.UpdateLastPlayedSongUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindUpdateLastPlayedSongUseCase(
        impl: UpdateLastPlayedSongUseCaseImpl
    ) : UpdateLastPlayedSongUseCase

    @Binds
    abstract fun bindGetLastPlayedSongUseCase(
        impl: GetLastPlayedSongUseCaseImpl
    ) : GetLastPlayedSongUseCase
}