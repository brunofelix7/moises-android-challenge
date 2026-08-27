package dev.brunofelix.moiseschallenge.feature.player.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
internal fun PlayerLandscapeContent(
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
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    device = "spec:width=1280dp,height=800dp,orientation=landscape"
)
@Composable
private fun PlayerLandscapeContentPreview() {
    AppTheme {
        PlayerLandscapeContent(
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
