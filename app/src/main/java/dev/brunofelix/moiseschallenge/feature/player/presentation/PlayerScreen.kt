package dev.brunofelix.moiseschallenge.feature.player.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme
import dev.brunofelix.moiseschallenge.feature.player.presentation.components.PlayerControls
import dev.brunofelix.moiseschallenge.feature.player.presentation.components.PlayerCover
import dev.brunofelix.moiseschallenge.feature.player.presentation.components.PlayerInfo
import dev.brunofelix.moiseschallenge.feature.player.presentation.components.PlayerSkeleton
import dev.brunofelix.moiseschallenge.feature.player.presentation.components.PlayerSlider

@Composable
internal fun PlayerRoute(
    songId: Long,
    onBack: () -> Unit,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val song by viewModel.song.collectAsStateWithLifecycle()
    val isPlaying by viewModel.playerState.collectAsStateWithLifecycle(initialValue = PlayerState.Playing) // ex: it == PlayerState.PLAYING
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle(initialValue = 0L)
    val duration by viewModel.duration.collectAsStateWithLifecycle(initialValue = 0L)
    val isRepeatEnabled by viewModel.repeatMode.collectAsStateWithLifecycle(initialValue = PlayerRepeatMode.OFF)

    LaunchedEffect(songId) {
        viewModel.init(songId)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopPlayback()
        }
    }

    PlayerScreen(
        song = song,
        isPlaying = isPlaying == PlayerState.Playing,
        currentPosition = currentPosition,
        totalDuration = duration,
        isRepeatEnabled = isRepeatEnabled == PlayerRepeatMode.ONE,
        onBack = onBack,
        onPlayPauseClick = {
            if (isPlaying == PlayerState.Playing) viewModel.pause() else viewModel.resume()
        },
        onPreviousClick = viewModel::moveBackward,
        onNextClick = viewModel::moveForward,
        onSeek = { position -> viewModel.seekTo(position.toLong()) },
        onRepeatClick = viewModel::toggleRepeatMode
    )
}

@Composable
internal fun PlayerScreen(
    song: Song?,
    isPlaying: Boolean,
    currentPosition: Long,
    totalDuration: Long,
    isRepeatEnabled: Boolean,
    onBack: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onSeek: (Float) -> Unit,
    onRepeatClick: () -> Unit,
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
                onBack = onBack,
                onAction = {}
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
}

@Composable
fun PlayerContent(
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        PlayerCover(coverUrl = song.coverUrl)
        Spacer(modifier = Modifier.weight(1f))
        PlayerInfo(
            title = song.title,
            artist = song.artist
        )
        PlayerSlider(
            currentPosition = currentPosition,
            totalDuration = totalDuration,
            onSeek = onSeek
        )
        Spacer(modifier = Modifier.height(16.dp))
        PlayerControls(
            isPlaying = isPlaying,
            isRepeatEnabled = isRepeatEnabled,
            onPlayPauseClick = onPlayPauseClick,
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick,
            onRepeatClick = onRepeatClick,
            modifier = Modifier.padding(bottom = 32.dp)
        )
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
            onBack = {},
            onPlayPauseClick = {},
            onPreviousClick = {},
            onNextClick = {},
            onSeek = {},
            isRepeatEnabled = false,
            onRepeatClick = {}
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
            onBack = {},
            onPlayPauseClick = {},
            onPreviousClick = {},
            onNextClick = {},
            onSeek = {},
            isRepeatEnabled = false,
            onRepeatClick = {}
        )
    }
}