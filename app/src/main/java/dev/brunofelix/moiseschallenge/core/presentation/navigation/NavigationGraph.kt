package dev.brunofelix.moiseschallenge.core.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dev.brunofelix.moiseschallenge.feature.splash.presentation.splashNavEntry
import dev.brunofelix.moiseschallenge.feature.album.presentation.albumNavEntry
import dev.brunofelix.moiseschallenge.feature.player.presentation.playerNavEntry
import dev.brunofelix.moiseschallenge.feature.song.presentation.songNavEntry

@Composable
fun NavigationGraph(
    backStack: List<Route>,
    onNavigate: (Route) -> Unit,
    onNavigateNext: (Route) -> Unit,
    onBack: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    val entryProvider = entryProvider {
        // Splash screen
        splashNavEntry(onNavigateNext)

        // Songs screen
        songNavEntry(onNavigate, paddingValues)

        // Player screen
        playerNavEntry(onNavigate, paddingValues)

        // Album screen
        albumNavEntry(onNavigate, paddingValues)
    }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = onBack,
        entryProvider = entryProvider
    )
}