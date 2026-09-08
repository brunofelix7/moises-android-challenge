package dev.brunofelix.moiseschallenge.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Route : NavKey {
    @Serializable
    data object Splash : Route

    @Serializable
    data object Songs : Route

    @Serializable
    data class Player(val songId: Long) : Route

    @Serializable
    data class Album(val albumId: Long) : Route
}