package dev.brunofelix.moiseschallenge.core.presentation.ui.design_system

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = appColorScheme,
        typography = appTypography,
        shapes = appShapes,
        content = content
    )
}