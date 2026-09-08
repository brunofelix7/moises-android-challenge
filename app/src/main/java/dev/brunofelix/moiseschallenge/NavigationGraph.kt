package dev.brunofelix.moiseschallenge

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.brunofelix.moiseschallenge.navigation.Route
import dev.brunofelix.moiseschallenge.presentation.albumNavEntry
import dev.brunofelix.moiseschallenge.presentation.playerNavEntry
import dev.brunofelix.moiseschallenge.presentation.songNavEntry
import dev.brunofelix.moiseschallenge.presentation.splashNavEntry

@Composable
fun NavigationGraph(
    backStack: List<Route>,
    onNavigate: (Route) -> Unit,
    onReplace: (Route) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val entryProvider = entryProvider {
        // Splash screen
        splashNavEntry(onReplace)

        // Songs screen
        songNavEntry(onNavigate)

        // Player screen
        playerNavEntry(onReplace, onBack)

        // Album screen
        albumNavEntry(onReplace, onBack)
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