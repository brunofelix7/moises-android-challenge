package dev.brunofelix.moiseschallenge.feature.album.presentation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.brunofelix.moiseschallenge.core.presentation.navigation.Route

fun EntryProviderScope<NavKey>.albumNavEntry(
    onNavigate: (Route) -> Unit,
    onBack: () -> Unit
) {
    entry<Route.Album> {
        AlbumRoute(
            albumId = it.albumId,
            onNavigate = onNavigate,
            onBack = onBack
        )
    }
}