package dev.brunofelix.moiseschallenge.presentation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.brunofelix.moiseschallenge.navigation.Route

fun EntryProviderScope<NavKey>.playerNavEntry(
    onReplace: (Route) -> Unit,
    onBack: () -> Unit
) {
    entry<Route.Player> { route ->
        PlayerRoute(
            songId = route.songId,
            onReplace = onReplace,
            onBack = onBack
        )
    }
}
