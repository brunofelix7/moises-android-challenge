package dev.brunofelix.moiseschallenge.feature.player.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.brunofelix.moiseschallenge.core.domain.util.extension.toItunesImageSize
import dev.brunofelix.moiseschallenge.core.presentation.components.AppAsyncImage

@Composable
internal fun PlayerCover(
    coverUrl: String,
    modifier: Modifier = Modifier,
    size: Dp = 264.dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        AppAsyncImage(
            model = coverUrl.toItunesImageSize(500),
            modifier = Modifier
                .width(size)
                .height(size)
                .aspectRatio(1F)
                .clip(MaterialTheme.shapes.large)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun Preview() {
    PlayerCover(coverUrl = "")
}