package dev.brunofelix.moiseschallenge.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.brunofelix.moiseschallenge.util.extension.toFormattedTime
import dev.brunofelix.moiseschallenge.util.extension.toSafeSliderRange
import dev.brunofelix.moiseschallenge.theme.AppTheme
import dev.brunofelix.moiseschallenge.theme.extraSmallSpacing
import dev.brunofelix.moiseschallenge.theme.sliderDarkGrayColor
import dev.brunofelix.moiseschallenge.theme.sliderLightGrayColor
import dev.brunofelix.moiseschallenge.theme.smallSpacing
import dev.brunofelix.moiseschallenge.theme.spacing16

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlayerSlider(
    currentPosition: Long,
    totalDuration: Long,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var dragPosition by remember { mutableStateOf<Float?>(null) }
    val displayPosition = dragPosition?.toLong() ?: currentPosition

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        CompositionLocalProvider(
            LocalMinimumInteractiveComponentSize provides 0.dp
        ) {
            Slider(
                value = dragPosition ?: currentPosition.toFloat(),
                valueRange = totalDuration.toSafeSliderRange(),
                onValueChange = { dragPosition = it },
                onValueChangeFinished = {
                    dragPosition?.let { finalPosition ->
                        onSeek(finalPosition)
                        dragPosition = null
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .compensateSliderPadding(thumbSize = spacing16),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = sliderLightGrayColor,
                    inactiveTrackColor = sliderDarkGrayColor
                ),
                track = { sliderState ->
                    SliderTrack(sliderState)
                },
                thumb = {
                    SliderThumb()
                }
            )
        }
        SliderTimers(
            displayPosition = displayPosition,
            totalDuration = totalDuration
        )
    }
}

@Composable
private fun SliderThumb(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(spacing16)
            .background(Color.White, CircleShape)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SliderTrack(
    sliderState: SliderState,
    modifier: Modifier = Modifier
) {
    SliderDefaults.Track(
        sliderState = sliderState,
        modifier = modifier.height(extraSmallSpacing),
        colors = SliderDefaults.colors(
            activeTrackColor = sliderLightGrayColor,
            inactiveTrackColor = sliderDarkGrayColor
        ),
        thumbTrackGapSize = 0.dp,
        drawStopIndicator = null
    )
}

@Composable
private fun SliderTimers(
    displayPosition: Long,
    totalDuration: Long,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = smallSpacing),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = displayPosition.toFormattedTime(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = (totalDuration - displayPosition).toFormattedTime(isRemaining = true),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

private fun Modifier.compensateSliderPadding(
    thumbSize: Dp
): Modifier = this.layout { measurable, constraints ->
    val horizontalExtra = thumbSize.roundToPx()
    val placeable = measurable.measure(
        constraints.copy(maxWidth = constraints.maxWidth + horizontalExtra)
    )
    layout(placeable.width - horizontalExtra, placeable.height) {
        placeable.placeRelative(-horizontalExtra / 2, 0)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun Preview() {
    AppTheme {
        PlayerSlider(
            currentPosition = 5000,
            totalDuration = 10000,
            onSeek = {}
        )
    }
}