package dev.brunofelix.moiseschallenge.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.brunofelix.moiseschallenge.R
import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.util.extension.toItunesImageSize
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme
import dev.brunofelix.moiseschallenge.core.presentation.design_system.extraSmallSpacing
import dev.brunofelix.moiseschallenge.core.presentation.design_system.greenColor
import dev.brunofelix.moiseschallenge.core.presentation.design_system.sliderDarkGrayColor
import dev.brunofelix.moiseschallenge.core.presentation.design_system.sliderLightGrayColor
import dev.brunofelix.moiseschallenge.core.presentation.design_system.smallSpacing
import dev.brunofelix.moiseschallenge.core.presentation.design_system.spacing16

@Composable
fun MiniPlayerBar(
    modifier: Modifier = Modifier,
    song: Song,
    isPlaying: Boolean,
    currentPosition: Long = 0L,
    totalDuration: Long = 0L,
    onClick: () -> Unit,
    onPlay: () -> Unit
) {
    val isEmpty = song.id == 0L || song.title.isBlank()
    val title = if (isEmpty) stringResource(R.string.mini_player_empty_title) else song.title
    val artist = if (isEmpty) stringResource(R.string.mini_player_empty_artist) else song.artist

    val progress = if (!isEmpty && totalDuration > 0L) {
        (currentPosition.toFloat() / totalDuration.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    Box(
        modifier = modifier
            .clickable(enabled = !isEmpty, onClick = onClick)
            .background(greenColor)
            .navigationBarsPadding(),
        contentAlignment = Alignment.TopCenter,
    ) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp),
            color = sliderLightGrayColor,
            trackColor = sliderDarkGrayColor
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppAsyncImage(
                model = song.coverUrl.toItunesImageSize(60),
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(smallSpacing))
            )
            Spacer(Modifier.width(spacing16))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(extraSmallSpacing))
                Text(
                    text = artist,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = if (isEmpty) 0.7f else 1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(
                enabled = !isEmpty,
                onClick = onPlay
            ) {
                Icon(
                    painter = if (isPlaying) {
                        painterResource(R.drawable.ic_pause)
                    } else {
                        painterResource(R.drawable.ic_play)
                    },
                    contentDescription = stringResource(R.string.cd_play_pause_button),
                    tint = if (isEmpty) Color.White.copy(alpha = 0.4f) else Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun Preview() {
    AppTheme {
        Column {
            MiniPlayerBar(
                song = Song(
                    id = 1,
                    title = "Numb",
                    artist = "Linkin Park"
                ),
                isPlaying = true,
                currentPosition = 30000L,
                totalDuration = 180000L,
                onClick = {},
                onPlay = {}
            )
            Spacer(Modifier.height(16.dp))
            MiniPlayerBar(
                song = Song(),
                isPlaying = false,
                onClick = {},
                onPlay = {}
            )
        }
    }
}