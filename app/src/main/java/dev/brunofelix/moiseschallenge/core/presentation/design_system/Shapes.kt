package dev.brunofelix.moiseschallenge.core.presentation.design_system

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val spacing16 = 16.dp
val spacing18 = 18.dp
val extraSmallSpacing = 4.dp
val smallSpacing = 8.dp
val mediumSpacing = 12.dp
val largeSpacing = 20.dp
val extraLargeSpacing = 32.dp

val appShapes = Shapes(
    extraSmall = RoundedCornerShape(extraSmallSpacing),
    small = RoundedCornerShape(smallSpacing),
    medium = RoundedCornerShape(mediumSpacing),
    large = RoundedCornerShape(largeSpacing),
    extraLarge = RoundedCornerShape(extraLargeSpacing)
)