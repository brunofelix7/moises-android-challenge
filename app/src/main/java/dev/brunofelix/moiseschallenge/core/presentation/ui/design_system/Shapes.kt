package dev.brunofelix.moiseschallenge.core.presentation.ui.design_system

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val smallSpacing = 8.dp
val mediumSpacing = 12.dp
val largeSpacing = 20.dp

val appShapes = Shapes(
    small = RoundedCornerShape(smallSpacing),
    medium = RoundedCornerShape(mediumSpacing),
    large = RoundedCornerShape(largeSpacing)
)