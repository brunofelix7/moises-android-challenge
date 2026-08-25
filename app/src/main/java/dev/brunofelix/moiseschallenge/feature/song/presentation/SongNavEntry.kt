package dev.brunofelix.moiseschallenge.feature.song.presentation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.brunofelix.moiseschallenge.core.presentation.navigation.Route

fun EntryProviderScope<NavKey>.songNavEntry(
    onNavigate: (Route) -> Unit
) {
    entry<Route.Songs> {
        SongRoute(
            onNavigate = onNavigate
        )
    }
}