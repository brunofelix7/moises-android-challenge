package dev.brunofelix.moiseschallenge.core.presentation.ui.design_system

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import dev.brunofelix.moiseschallenge.R

val interFamily = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold),
)

val appTypography = Typography(
    titleLarge = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = interFamily,
        lineHeight = 1.2.em,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = interFamily,
        lineHeight = 1.2.em,
        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = interFamily,
        lineHeight = 1.08.em,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = interFamily,
        lineHeight = 1.2.em,
        letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = interFamily,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp
    )
)