package dev.brunofelix.moiseschallenge.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme
import dev.brunofelix.moiseschallenge.core.presentation.design_system.extraSmallSpacing
import dev.brunofelix.moiseschallenge.core.presentation.design_system.mediumSpacing
import dev.brunofelix.moiseschallenge.core.presentation.design_system.smallSpacing
import dev.brunofelix.moiseschallenge.core.presentation.design_system.spacing16
import dev.brunofelix.moiseschallenge.core.presentation.design_system.spacing24
import dev.brunofelix.moiseschallenge.core.presentation.util.shimmerEffect

@Composable
fun SongItemSkeleton(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .height(68.dp)
            .padding(start = spacing16, end = smallSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(MaterialTheme.shapes.small)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.width(spacing16))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(spacing16)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(extraSmallSpacing))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(mediumSpacing)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect()
            )
        }
        Box(
            modifier = Modifier
                .padding(mediumSpacing)
                .size(spacing24)
                .clip(CircleShape)
                .shimmerEffect()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SongItemSkeletonPreview() {
    AppTheme {
        Column {
            repeat(5) {
                SongItemSkeleton()
            }
        }
    }
}
