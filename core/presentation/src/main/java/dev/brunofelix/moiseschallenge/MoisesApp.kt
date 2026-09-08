package dev.brunofelix.moiseschallenge

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventDispatcher
import androidx.navigationevent.NavigationEventDispatcherOwner
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import dev.brunofelix.moiseschallenge.components.MiniPlayerBar
import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.viewmodel.NavigationViewModel
import dev.brunofelix.moiseschallenge.navigation.Route
import dev.brunofelix.moiseschallenge.player.PlayerState
import dev.brunofelix.moiseschallenge.theme.AppTheme
import dev.brunofelix.moiseschallenge.util.UiState
import dev.brunofelix.moiseschallenge.viewmodel.MiniPlayerViewModel

@Composable
fun MoisesApp(
    modifier: Modifier = Modifier,
    viewModel: NavigationViewModel = hiltViewModel(),
    miniPlayerViewModel: MiniPlayerViewModel = hiltViewModel(),
    navigationContent: @Composable (PaddingValues) -> Unit = {}
) {
    val backStack by viewModel.backStack.collectAsStateWithLifecycle()
    val playerState by miniPlayerViewModel.playerState.collectAsStateWithLifecycle(
        initialValue = null
    )
    val uiState by miniPlayerViewModel.uiState.collectAsStateWithLifecycle()
    val currentPosition by miniPlayerViewModel.currentPosition.collectAsStateWithLifecycle(
        initialValue = 0L
    )
    val duration by miniPlayerViewModel.duration.collectAsStateWithLifecycle(
        initialValue = 0L
    )
    val currentRoute = backStack.lastOrNull()
    val showMiniPlayer = currentRoute != null &&
            currentRoute !is Route.Splash &&
            currentRoute !is Route.Player

    MoisesAppContent(
        modifier = modifier,
        onNavigate = viewModel::navigateTo,
        uiState = uiState,
        showMiniPlayer = showMiniPlayer,
        isPlaying = playerState == PlayerState.Playing,
        currentPosition = currentPosition,
        totalDuration = duration,
        onPlay = {
            if (playerState == PlayerState.Playing) {
                miniPlayerViewModel.pause()
            } else {
                miniPlayerViewModel.resume()
            }
        },
        navigationContent = navigationContent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoisesAppContent(
    modifier: Modifier = Modifier,
    onNavigate: (Route) -> Unit,
    onPlay: () -> Unit,
    showMiniPlayer: Boolean,
    uiState: UiState<Song>,
    isPlaying: Boolean,
    currentPosition: Long = 0L,
    totalDuration: Long = 0L,
    navigationContent: @Composable (PaddingValues) -> Unit = {}
) {
    Scaffold(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            AnimatedVisibility(
                visible = showMiniPlayer
            ) {
                val song = uiState as? UiState.Success
                MiniPlayerBar(
                    song = song?.data ?: Song(),
                    isPlaying = isPlaying,
                    currentPosition = currentPosition,
                    totalDuration = totalDuration,
                    onClick = {
                        if (song != null) {
                            onNavigate(Route.Player(song.data.id))
                        }
                    },
                    onPlay = onPlay
                )
            }
        },
        content = { innerPadding ->
            navigationContent(innerPadding)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MoisesAppPreview() {
    val dispatcher = remember { NavigationEventDispatcher() }
    val owner = remember {
        object : NavigationEventDispatcherOwner {
            override val navigationEventDispatcher = dispatcher
        }
    }
    AppTheme {
        CompositionLocalProvider(LocalNavigationEventDispatcherOwner provides owner) {
            MoisesAppContent(
                onNavigate = {},
                onPlay = {},
                showMiniPlayer = true,
                uiState = UiState.Success(
                    Song(
                        id = 1,
                        title = "Numb",
                        artist = "Linkin Park"
                    )
                ),
                isPlaying = true,
                currentPosition = 30000L,
                totalDuration = 180000L
            )
        }
    }
}