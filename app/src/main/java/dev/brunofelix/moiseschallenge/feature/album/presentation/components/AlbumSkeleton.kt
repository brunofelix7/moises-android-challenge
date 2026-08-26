package dev.brunofelix.moiseschallenge.feature.album.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.brunofelix.moiseschallenge.core.presentation.components.SongItemSkeleton
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme
import dev.brunofelix.moiseschallenge.core.presentation.design_system.extraLargeSpacing
import dev.brunofelix.moiseschallenge.core.presentation.design_system.smallSpacing
import dev.brunofelix.moiseschallenge.core.presentation.design_system.spacing16
import dev.brunofelix.moiseschallenge.core.presentation.design_system.spacing24
import dev.brunofelix.moiseschallenge.core.presentation.util.shimmerEffect

@Composable
internal fun AlbumSkeleton(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = extraLargeSpacing)
    ) {
        item {
            AlbumHeaderSkeleton(
                modifier = Modifier.padding(vertical = extraLargeSpacing)
            )
        }
        items(10) {
            SongItemSkeleton()
        }
    }
}

@Composable
private fun AlbumHeaderSkeleton(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(MaterialTheme.shapes.large)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(spacing24))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(extraLargeSpacing)
                .clip(MaterialTheme.shapes.extraSmall)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(smallSpacing))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(spacing16)
                .clip(MaterialTheme.shapes.extraSmall)
                .shimmerEffect()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun AlbumSkeletonPreview() {
    AppTheme {
        AlbumSkeleton()
    }
}
