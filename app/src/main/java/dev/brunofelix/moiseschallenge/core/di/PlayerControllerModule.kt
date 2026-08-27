package dev.brunofelix.moiseschallenge.core.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.moiseschallenge.core.data.player.ExoPlayerControllerImpl
import dev.brunofelix.moiseschallenge.core.domain.player.PlayerController
import dev.brunofelix.moiseschallenge.core.domain.util.extension.toMegabits
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlayerControllerModule {

    @Binds
    @Singleton
    abstract fun bindPlayerController(
        impl: ExoPlayerControllerImpl
    ): PlayerController

    @OptIn(UnstableApi::class)
    companion object {

        @Provides
        @Singleton
        fun providePlayerCache(
            @ApplicationContext context: Context
        ): Cache {
            val cacheDir = File(context.cacheDir, "media_cache")
            val databaseProvider = StandaloneDatabaseProvider(context)
            return SimpleCache(
                cacheDir,
                LeastRecentlyUsedCacheEvictor(100.toMegabits()),
                databaseProvider
            )
        }

        @Provides
        @Singleton
        fun providePlayerDataSourceFactory(
            @ApplicationContext context: Context,
            cache: Cache
        ): CacheDataSource.Factory {
            val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            val defaultDataSourceFactory = DefaultDataSource.Factory(context, httpDataSourceFactory)
            return CacheDataSource.Factory()
                .setCache(cache)
                .setUpstreamDataSourceFactory(defaultDataSourceFactory)
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        }

        @Provides
        @Singleton
        fun provideExoPlayer(
            @ApplicationContext context: Context,
            dataSourceFactory: CacheDataSource.Factory
        ): ExoPlayer {
            return ExoPlayer.Builder(context)
                .setMediaSourceFactory(
                    DefaultMediaSourceFactory(context).setDataSourceFactory(dataSourceFactory)
                )
                .build()
        }
    }
}
