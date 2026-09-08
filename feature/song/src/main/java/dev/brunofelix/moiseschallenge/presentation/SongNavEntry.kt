package dev.brunofelix.moiseschallenge.presentation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.brunofelix.moiseschallenge.navigation.Route

fun EntryProviderScope<NavKey>.songNavEntry(
    onNavigate: (Route) -> Unit
) {
    entry<Route.Songs> {
        SongRoute(
            onNavigate = onNavigate
        )
    }
}