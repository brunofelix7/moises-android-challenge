package dev.brunofelix.moiseschallenge.feature.player.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.brunofelix.moiseschallenge.core.presentation.navigation.Route

fun EntryProviderScope<NavKey>.playerNavEntry(
    onNavigate: (Route) -> Unit,
    paddingValues: PaddingValues
) {
    entry<Route.Player> {
        // PlayerRoute
    }
}