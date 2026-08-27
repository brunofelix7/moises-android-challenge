package dev.brunofelix.moiseschallenge.feature.player.presentation

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.brunofelix.moiseschallenge.R
import dev.brunofelix.moiseschallenge.core.presentation.components.AppTopBar
import dev.brunofelix.moiseschallenge.core.presentation.components.SongActionSheet
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme
import dev.brunofelix.moiseschallenge.core.presentation.navigation.Route
import dev.brunofelix.moiseschallenge.feature.player.presentation.components.PlayerLandscapeContent
import dev.brunofelix.moiseschallenge.feature.player.presentation.components.PlayerPortraitContent
import dev.brunofelix.moiseschallenge.feature.player.presentation.components.PlayerSkeleton

@Composable
internal fun PlayerRoute(
    songId: Long,
    onReplace: (Route) -> Unit,
    onBack: () -> Unit,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isSheetVisible by remember { mutableStateOf(false) }
    val currentUiState = uiState.copy(isSheetVisible = isSheetVisible)

    BackHandler(enabled = true) {
        onBack()
    }

    LaunchedEffect(songId) {
        viewModel.init(songId)
    }

    PlayerScreen(
        uiState = currentUiState,
        onAction = { action ->
            when (action) {
                PlayerUiAction.OnPlayPause -> {
                    if (currentUiState.isPlaying) viewModel.pause() else viewModel.resume()
                }
                PlayerUiAction.OnPrevious -> viewModel.moveBackward()
                PlayerUiAction.OnNext -> viewModel.moveForward()
                is PlayerUiAction.OnSeek -> viewModel.seekTo(action.position.toLong())
                PlayerUiAction.OnToggleRepeat -> viewModel.toggleRepeatMode()
                PlayerUiAction.OnBack -> onBack()
                PlayerUiAction.OnActionClick -> isSheetVisible = true
                PlayerUiAction.OnDismissSheet -> isSheetVisible = false
                PlayerUiAction.OnViewAlbumClick -> {
                    isSheetVisible = false
                    currentUiState.song?.albumId?.let { albumId ->
                        onReplace(Route.Album(albumId))
                    }
                }
            }
        }
    )
}

@Composable
internal fun PlayerScreen(
    uiState: PlayerUiState,
    onAction: (PlayerUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                title = stringResource(R.string.now_playing),
                titleStyle = MaterialTheme.typography.titleSmall,
                actionIcon = R.drawable.ic_more,
                onBack = { onAction(PlayerUiAction.OnBack) },
                onAction = { onAction(PlayerUiAction.OnActionClick) }
            )
        }
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)
        val configuration = LocalConfiguration.current
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        when (uiState.song) {
            null -> {
                PlayerSkeleton(modifier = contentModifier)
            }
            else -> {
                if (isLandscape) {
                    PlayerLandscapeContent(
                        uiState = uiState,
                        onAction = onAction,
                        modifier = contentModifier
                    )
                } else {
                    PlayerPortraitContent(
                        uiState = uiState,
                        onAction = onAction,
                        modifier = contentModifier
                    )
                }
            }
        }
    }
    if (uiState.isSheetVisible && uiState.song != null) {
        SongActionSheet(
            songName = uiState.song.title,
            artistName = uiState.song.artist,
            onDismiss = { onAction(PlayerUiAction.OnDismissSheet) },
            onViewAlbumClick = { onAction(PlayerUiAction.OnViewAlbumClick) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingPreview() {
    AppTheme {
        PlayerScreen(
            uiState = PlayerUiState(),
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayingPreview() {
    AppTheme {
        PlayerScreen(
            uiState = PlayerUiState.preview,
            onAction = {}
        )
    }
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,orientation=landscape")
@Composable
private fun LandscapePreview() {
    AppTheme {
        PlayerScreen(
            uiState = PlayerUiState.preview,
            onAction = {}
        )
    }
}

@Preview(showBackground = true, device = "spec:width=673dp,height=841dp")
@Composable
private fun FoldablePreview() {
    AppTheme {
        PlayerScreen(
            uiState = PlayerUiState.preview,
            onAction = {}
        )
    }
}
