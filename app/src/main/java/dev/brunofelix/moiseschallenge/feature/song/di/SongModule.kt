package dev.brunofelix.moiseschallenge.feature.song.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.DeleteRecentSongUseCase
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.DeleteRecentSongUseCaseImpl
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.GetRecentlyPlayedSongsUseCase
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.GetRecentlyPlayedSongsUseCaseImpl
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.SaveRecentSongUseCase
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.SaveRecentSongUseCaseImpl
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.SearchSongsUseCase
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.SearchSongsUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class SongModule {

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
