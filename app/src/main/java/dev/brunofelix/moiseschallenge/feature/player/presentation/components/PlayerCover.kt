package dev.brunofelix.moiseschallenge.feature.player.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.brunofelix.moiseschallenge.core.domain.util.extension.toItunesImageSize
import dev.brunofelix.moiseschallenge.core.presentation.design_system.shimmerColorSecondary

@Composable
internal fun PlayerCover(
    coverUrl: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 70.dp)
    ) {
        AsyncImage(
            model = coverUrl.toItunesImageSize(500),
            contentDescription = null,
            placeholder = ColorPainter(shimmerColorSecondary),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier
                .width(264.dp)
                .height(264.dp)
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