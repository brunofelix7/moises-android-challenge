package dev.brunofelix.moiseschallenge.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.moiseschallenge.use_case.DeleteRecentSongUseCase
import dev.brunofelix.moiseschallenge.use_case.GetAlbumByIdUseCase
import dev.brunofelix.moiseschallenge.use_case.GetLastPlayedSongUseCase
import dev.brunofelix.moiseschallenge.use_case.GetRecentlyPlayedSongsUseCase
import dev.brunofelix.moiseschallenge.use_case.GetSavedSongByIdUseCase
import dev.brunofelix.moiseschallenge.use_case.SaveRecentSongUseCase
import dev.brunofelix.moiseschallenge.use_case.SearchSongsUseCase
import dev.brunofelix.moiseschallenge.use_case.UpdateLastPlayedSongUseCase
import dev.brunofelix.moiseschallenge.use_case.DeleteRecentSongUseCaseImpl
import dev.brunofelix.moiseschallenge.use_case.GetAlbumByIdUseCaseImpl
import dev.brunofelix.moiseschallenge.use_case.GetLastPlayedSongUseCaseImpl
import dev.brunofelix.moiseschallenge.use_case.GetRecentlyPlayedSongsUseCaseImpl
import dev.brunofelix.moiseschallenge.use_case.GetSavedSongByIdUseCaseImpl
import dev.brunofelix.moiseschallenge.use_case.SaveRecentSongUseCaseImpl
import dev.brunofelix.moiseschallenge.use_case.SearchSongsUseCaseImpl
import dev.brunofelix.moiseschallenge.use_case.UpdateLastPlayedSongUseCaseImpl

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

    @Binds
    abstract fun bindGetAlbumByIdUseCase(
        impl: GetAlbumByIdUseCaseImpl
    ): GetAlbumByIdUseCase

    @Binds
    abstract fun bindGetSavedSongByIdUseCase(
        impl: GetSavedSongByIdUseCaseImpl
    ): GetSavedSongByIdUseCase

    @Binds
    abstract fun bindSearchSongsUseCase(
        impl: SearchSongsUseCaseImpl
    ): SearchSongsUseCase

    @Binds
    abstract fun bindGetRecentlyPlayedSongsUseCase(
        impl: GetRecentlyPlayedSongsUseCaseImpl
    ): GetRecentlyPlayedSongsUseCase

    @Binds
    abstract fun bindSaveRecentSongUseCase(
        impl: SaveRecentSongUseCaseImpl
    ): SaveRecentSongUseCase

    @Binds
    abstract fun bindDeleteRecentSongUseCase(
        impl: DeleteRecentSongUseCaseImpl
    ): DeleteRecentSongUseCase
}