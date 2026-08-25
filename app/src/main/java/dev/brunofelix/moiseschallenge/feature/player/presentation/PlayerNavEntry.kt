package dev.brunofelix.moiseschallenge.feature.player.presentation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.brunofelix.moiseschallenge.core.presentation.navigation.Route

fun EntryProviderScope<NavKey>.playerNavEntry(
    onBack: () -> Unit
) {
    entry<Route.Player> { route ->
        PlayerRoute(
            songId = route.songId,
            onBack = onBack
        )
    }
}