package dev.brunofelix.moiseschallenge.core.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.moiseschallenge.core.data.player.ExoPlayerControllerImpl
import dev.brunofelix.moiseschallenge.core.domain.player.PlayerController
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlayerModule {

    @Binds
    @Singleton
    abstract fun bindPlayerController(
        impl: ExoPlayerControllerImpl
    ): PlayerController

    companion object {

        @Provides
        @Singleton
        fun provideExoPlayer(
            @ApplicationContext context: Context
        ): ExoPlayer {
            return ExoPlayer.Builder(context).build()
        }
    }
}