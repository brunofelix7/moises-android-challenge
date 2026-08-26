package dev.brunofelix.moiseschallenge.feature.player.presentation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.brunofelix.moiseschallenge.core.presentation.navigation.Route

fun EntryProviderScope<NavKey>.playerNavEntry(
    onNavigate: (Route) -> Unit,
    onBack: () -> Unit
) {
    entry<Route.Player> { route ->
        PlayerRoute(
            songId = route.songId,
            onNavigate = onNavigate,
            onBack = onBack
        )
    }
}