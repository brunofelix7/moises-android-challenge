package dev.brunofelix.moiseschallenge.feature.splash.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.brunofelix.moiseschallenge.R
import dev.brunofelix.moiseschallenge.core.presentation.navigation.Route
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun SplashRoute(
    onReplace: (Route) -> Unit
) {
    LaunchedEffect(key1 = true) {
        delay(1500.milliseconds)
        onReplace(Route.Songs)
    }
    SplashScreen()
}

@Composable
internal fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashDefaults.backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
    }
}

private object SplashDefaults {
    val backgroundBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF093A43),
            Color(0xFF000000),
            Color(0xFF000000)
        ),
        start = Offset(Float.POSITIVE_INFINITY, 0F),
        end = Offset(0F, Float.POSITIVE_INFINITY)
    )
}

@Preview
@Composable
private fun Preview() {
    SplashScreen()
}