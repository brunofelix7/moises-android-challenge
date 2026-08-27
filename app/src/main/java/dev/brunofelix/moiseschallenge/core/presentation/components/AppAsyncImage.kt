package dev.brunofelix.moiseschallenge.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import dev.brunofelix.moiseschallenge.R
import dev.brunofelix.moiseschallenge.core.presentation.design_system.shimmerColorSecondary

@Composable
fun AppAsyncImage(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String = stringResource(R.string.cd_album_cover),
    placeholder: Painter? = ColorPainter(shimmerColorSecondary),
    error: Painter? = ColorPainter(shimmerColorSecondary),
    fallback: Painter? = error,
    contentScale: ContentScale = ContentScale.Crop,
    alignment: Alignment = Alignment.Center
) {
    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        placeholder = placeholder,
        error = error,
        fallback = fallback,
        contentScale = contentScale,
        alignment = alignment
    )
}
