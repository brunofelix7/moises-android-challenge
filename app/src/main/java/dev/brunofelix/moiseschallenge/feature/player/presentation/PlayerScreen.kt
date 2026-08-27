package dev.brunofelix.moiseschallenge.feature.player.presentation

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.brunofelix.moiseschallenge.R
import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.player.PlayerRepeatMode
import dev.brunofelix.moiseschallenge.core.domain.player.PlayerState
import dev.brunofelix.moiseschallenge.core.presentation.components.AppTopBar
import dev.brunofelix.moiseschallenge.core.presentation.components.SongActionSheet
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme
import dev.brunofelix.moiseschallenge.core.presentation.navigation.Route
import dev.brunofelix.moiseschallenge.feature.player.presentation.components.PlayerControls
import dev.brunofelix.moiseschallenge.feature.player.presentation.components.PlayerCover
import dev.brunofelix.moiseschallenge.feature.player.presentation.components.PlayerInfo
import dev.brunofelix.moiseschallenge.feature.player.presentation.components.PlayerSkeleton
import dev.brunofelix.moiseschallenge.feature.player.presentation.components.PlayerSlider

@Composable
internal fun PlayerRoute(
    songId: Long,
    onReplace: (Route) -> Unit,
    onBack: () -> Unit,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val song by viewModel.song.collectAsStateWithLifecycle()
    val isPlaying by viewModel.playerState.collectAsStateWithLifecycle(initialValue = PlayerState.Playing)
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle(initialValue = 0L)
    val duration by viewModel.duration.collectAsStateWithLifecycle(initialValue = 0L)
    val isRepeatEnabled by viewModel.repeatMode.collectAsStateWithLifecycle(initialValue = PlayerRepeatMode.OFF)
    var isSheetVisible by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        onBack()
    }

    LaunchedEffect(songId) {
        viewModel.init(songId)
    }

    PlayerScreen(
        song = song,
        isPlaying = isPlaying == PlayerState.Playing,
        currentPosition = currentPosition,
        totalDuration = duration,
        isRepeatEnabled = isRepeatEnabled == PlayerRepeatMode.ONE,
        isSheetVisible = isSheetVisible,
        onBack = onBack,
        onPlayPauseClick = {
            if (isPlaying == PlayerState.Playing) viewModel.pause() else viewModel.resume()
        },
        onPreviousClick = viewModel::moveBackward,
        onNextClick = viewModel::moveForward,
        onSeek = { position -> viewModel.seekTo(position.toLong()) },
        onRepeatClick = viewModel::toggleRepeatMode,
        onActionClick = { isSheetVisible = true },
        onDismissSheet = { isSheetVisible = false },
        onViewAlbumClick = {
            isSheetVisible = false
            song?.albumId?.let { albumId ->
                onReplace(Route.Album(albumId))
            }
        }
    )
}

@Composable
internal fun PlayerScreen(
    modifier: Modifier = Modifier,
    song: Song?,
    isPlaying: Boolean,
    currentPosition: Long,
    totalDuration: Long,
    isRepeatEnabled: Boolean,
    isSheetVisible: Boolean,
    onBack: () -> Unit = {},
    onPlayPauseClick: () -> Unit = {},
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onSeek: (Float) -> Unit = {},
    onRepeatClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
    onDismissSheet: () -> Unit = {},
    onViewAlbumClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                title = stringResource(R.string.now_playing),
                titleStyle = MaterialTheme.typography.titleSmall,
                actionIcon = R.drawable.ic_more,
                onBack = onBack,
                onAction = onActionClick
            )
        }
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)

        when (song) {
            null -> {
                PlayerSkeleton(modifier = contentModifier)
            }
            else -> {
                PlayerContent(
                    song = song,
                    isPlaying = isPlaying,
                    currentPosition = currentPosition,
                    totalDuration = totalDuration,
                    isRepeatEnabled = isRepeatEnabled,
                    onPlayPauseClick = onPlayPauseClick,
                    onPreviousClick = onPreviousClick,
                    onNextClick = onNextClick,
                    onSeek = onSeek,
                    onRepeatClick = onRepeatClick,
                    modifier = contentModifier
                )
            }
        }
    }

    if (isSheetVisible && song != null) {
        SongActionSheet(
            songName = song.title,
            artistName = song.artist,
            onDismiss = onDismissSheet,
            onViewAlbumClick = onViewAlbumClick
        )
    }
}

@Composable
internal fun PlayerContent(
    song: Song,
    isPlaying: Boolean,
    currentPosition: Long,
    totalDuration: Long,
    isRepeatEnabled: Boolean,
    onPlayPauseClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onSeek: (Float) -> Unit,
    onRepeatClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val scrollState = rememberScrollState()

    if (isLandscape) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                PlayerCover(
                    coverUrl = song.coverUrl,
                    size = 200.dp
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            Column(
                modifier = Modifier
                    .weight(1.2f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                PlayerInfo(
                    title = song.title,
                    artist = song.artist
                )
                Spacer(modifier = Modifier.height(20.dp))
                PlayerSlider(
                    currentPosition = currentPosition,
                    totalDuration = totalDuration,
                    onSeek = onSeek
                )
                Spacer(modifier = Modifier.height(20.dp))
                PlayerControls(
                    isPlaying = isPlaying,
                    isRepeatEnabled = isRepeatEnabled,
                    onPlayPauseClick = onPlayPauseClick,
                    onPreviousClick = onPreviousClick,
                    onNextClick = onNextClick,
                    onRepeatClick = onRepeatClick
                )
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                PlayerCover(
                    coverUrl = song.coverUrl,
                    modifier = Modifier.padding(top = 0.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                PlayerInfo(
                    title = song.title,
                    artist = song.artist
                )
                Spacer(modifier = Modifier.height(20.dp))
                PlayerSlider(
                    currentPosition = currentPosition,
                    totalDuration = totalDuration,
                    onSeek = onSeek
                )
                Spacer(modifier = Modifier.height(20.dp))
                PlayerControls(
                    isPlaying = isPlaying,
                    isRepeatEnabled = isRepeatEnabled,
                    onPlayPauseClick = onPlayPauseClick,
                    onPreviousClick = onPreviousClick,
                    onNextClick = onNextClick,
                    onRepeatClick = onRepeatClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingPreview() {
    AppTheme {
        PlayerScreen(
            song = null,
            isPlaying = false,
            currentPosition = 0L,
            totalDuration = 0L,
            isRepeatEnabled = false,
            isSheetVisible = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayingPreview() {
    AppTheme {
        PlayerScreen(
            song = Song(
                id = 1,
                title = "Get Lucky",
                artist = "Daft Punk feat. Pharrell Williams",
            ),
            isPlaying = true,
            currentPosition = 86000L,
            totalDuration = 260000L,
            isRepeatEnabled = false,
            isSheetVisible = false
        )
    }
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,orientation=landscape")
@Composable
private fun LandscapePreview() {
    AppTheme {
        PlayerScreen(
            song = Song(
                id = 1,
                title = "Get Lucky",
                artist = "Daft Punk feat. Pharrell Williams",
            ),
            isPlaying = true,
            currentPosition = 86000L,
            totalDuration = 260000L,
            isRepeatEnabled = false,
            isSheetVisible = false
        )
    }
}

@Preview(showBackground = true, device = "spec:width=673dp,height=841dp")
@Composable
private fun FoldablePreview() {
    AppTheme {
        PlayerScreen(
            song = Song(
                id = 1,
                title = "Get Lucky",
                artist = "Daft Punk feat. Pharrell Williams",
            ),
            isPlaying = true,
            currentPosition = 86000L,
            totalDuration = 260000L,
            isRepeatEnabled = false,
            isSheetVisible = false
        )
    }
}
