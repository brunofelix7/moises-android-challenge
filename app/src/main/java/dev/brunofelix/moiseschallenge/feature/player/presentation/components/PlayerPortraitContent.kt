package dev.brunofelix.moiseschallenge.feature.player.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme
import dev.brunofelix.moiseschallenge.feature.player.presentation.PlayerUiAction
import dev.brunofelix.moiseschallenge.feature.player.presentation.PlayerUiState

@Composable
internal fun PlayerPortraitContent(
    uiState: PlayerUiState,
    onAction: (PlayerUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val song = uiState.song ?: return
    val scrollState = rememberScrollState()

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
                currentPosition = uiState.currentPosition,
                totalDuration = uiState.totalDuration,
                onSeek = { position -> onAction(PlayerUiAction.OnSeek(position)) }
            )
            Spacer(modifier = Modifier.height(20.dp))
            PlayerControls(
                isPlaying = uiState.isPlaying,
                isRepeatEnabled = uiState.isRepeatEnabled,
                onPlayPauseClick = { onAction(PlayerUiAction.OnPlayPause) },
                onPreviousClick = { onAction(PlayerUiAction.OnPrevious) },
                onNextClick = { onAction(PlayerUiAction.OnNext) },
                onRepeatClick = { onAction(PlayerUiAction.OnToggleRepeat) }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PlayerPortraitContentPreview() {
    AppTheme {
        PlayerPortraitContent(
            uiState = PlayerUiState(
                song = Song(
                    id = 1,
                    title = "Get Lucky",
                    artist = "Daft Punk feat. Pharrell Williams",
                ),
                isPlaying = true,
                currentPosition = 86000L,
                totalDuration = 260000L,
                isRepeatEnabled = false
            ),
            onAction = {}
        )
    }
}
