package dev.brunofelix.moiseschallenge.feature.album.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.brunofelix.moiseschallenge.core.presentation.navigation.Route

fun EntryProviderScope<NavKey>.albumNavEntry(
    onNavigate: (Route) -> Unit,
    paddingValues: PaddingValues
) {
    entry<Route.Album> {
        // AlbumRoute
    }
}