package dev.brunofelix.moiseschallenge.feature.player.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.brunofelix.moiseschallenge.R
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme
import dev.brunofelix.moiseschallenge.core.presentation.design_system.greenColor
import dev.brunofelix.moiseschallenge.core.presentation.design_system.playerGrayColor

@Composable
internal fun PlayerControls(
    isPlaying: Boolean,
    isRepeatEnabled: Boolean,
    onPlayPauseClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onRepeatClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(playerGrayColor)
                .clickable(onClick = onPlayPauseClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = if (isPlaying) painterResource(R.drawable.ic_pause) else painterResource(R.drawable.ic_play),
                contentDescription = stringResource(R.string.cd_play_pause_button),
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.width(24.dp))
        IconButton(onClick = onPreviousClick) {
            Icon(
                painter = painterResource(R.drawable.ic_backward_bar_fill),
                contentDescription = stringResource(R.string.cd_move_backward_button),
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.width(24.dp))
        IconButton(onClick = onNextClick) {
            Icon(
                painter = painterResource(R.drawable.ic_forward_bar_fill),
                contentDescription = stringResource(R.string.cd_move_forward_button),
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onRepeatClick) {
            Icon(
                painter = painterResource(R.drawable.ic_play_on_repeat),
                contentDescription = stringResource(R.string.cd_repeat_button),
                tint = if (isRepeatEnabled) greenColor else MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PlayPreview() {
    AppTheme {
        PlayerControls(
            isPlaying = true,
            isRepeatEnabled = true,
            onPlayPauseClick = {},
            onPreviousClick = {},
            onNextClick = {},
            onRepeatClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PausePreview() {
    AppTheme {
        PlayerControls(
            isPlaying = false,
            isRepeatEnabled = false,
            onPlayPauseClick = {},
            onPreviousClick = {},
            onNextClick = {},
            onRepeatClick = {}
        )
    }
}

