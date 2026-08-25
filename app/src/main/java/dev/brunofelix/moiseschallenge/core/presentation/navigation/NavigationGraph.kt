package dev.brunofelix.moiseschallenge.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.brunofelix.moiseschallenge.feature.album.presentation.albumNavEntry
import dev.brunofelix.moiseschallenge.feature.player.presentation.playerNavEntry
import dev.brunofelix.moiseschallenge.feature.song.presentation.songNavEntry
import dev.brunofelix.moiseschallenge.feature.splash.presentation.splashNavEntry

@Composable
fun NavigationGraph(
    backStack: List<Route>,
    onNavigate: (Route) -> Unit,
    onNavigateNext: (Route) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val entryProvider = entryProvider {
        // Splash screen
        splashNavEntry(onNavigateNext)

        // Songs screen
        songNavEntry(onNavigate)

        // Player screen
        playerNavEntry(onNavigate, onBack)

        // Album screen
        albumNavEntry(onNavigate, onBack)
    }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = onBack,
        entryProvider = entryProvider,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        )
    )
}