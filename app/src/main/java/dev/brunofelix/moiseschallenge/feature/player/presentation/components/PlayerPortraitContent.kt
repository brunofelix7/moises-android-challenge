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

@Composable
internal fun PlayerPortraitContent(
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

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PlayerPortraitContentPreview() {
    AppTheme {
        PlayerPortraitContent(
            song = Song(
                id = 1,
                title = "Get Lucky",
                artist = "Daft Punk feat. Pharrell Williams",
            ),
            isPlaying = true,
            currentPosition = 86000L,
            totalDuration = 260000L,
            isRepeatEnabled = false,
            onPlayPauseClick = {},
            onPreviousClick = {},
            onNextClick = {},
            onSeek = {},
            onRepeatClick = {}
        )
    }
}
