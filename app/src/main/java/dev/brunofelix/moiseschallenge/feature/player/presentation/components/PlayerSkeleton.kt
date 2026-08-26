package dev.brunofelix.moiseschallenge.feature.player.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme
import dev.brunofelix.moiseschallenge.core.presentation.util.shimmerEffect

@Composable
fun PlayerSkeleton(
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val scrollState = rememberScrollState()

    if (isLandscape) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cover Skeleton
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .shimmerEffect()
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            // Info & Controls Skeleton
            Column(
                modifier = Modifier
                    .weight(1.2f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                SkeletonInfoAndControls()
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            // Cover Area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(264.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .shimmerEffect()
                )
            }

            // Controls Area (Fixed Bottom)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                SkeletonInfoAndControls()
            }
        }
    }
}

@Composable
private fun SkeletonInfoAndControls() {
    // Music Title
    Box(
        modifier = Modifier
            .fillMaxWidth(0.65f)
            .height(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .shimmerEffect()
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Artist Name
    Box(
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .height(20.dp)
            .clip(RoundedCornerShape(6.dp))
            .shimmerEffect()
    )

    Spacer(modifier = Modifier.height(24.dp))

    // Slider
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp))
            .shimmerEffect()
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Timers
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(width = 36.dp, height = 14.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .size(width = 40.dp, height = 14.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect()
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Controls
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Play/Pause
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.width(32.dp))

        // Previous
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Next
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.weight(1f))

        // Repeat
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .shimmerEffect()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun Preview() {
    AppTheme {
        PlayerSkeleton()
    }
}