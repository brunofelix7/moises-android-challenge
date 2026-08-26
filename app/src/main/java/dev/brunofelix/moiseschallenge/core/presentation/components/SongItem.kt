package dev.brunofelix.moiseschallenge.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.brunofelix.moiseschallenge.R
import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.util.extension.toItunesImageSize
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme
import dev.brunofelix.moiseschallenge.core.presentation.design_system.extraSmallSpacing
import dev.brunofelix.moiseschallenge.core.presentation.design_system.infoGrayColor
import dev.brunofelix.moiseschallenge.core.presentation.design_system.shimmerColorSecondary
import dev.brunofelix.moiseschallenge.core.presentation.design_system.smallSpacing
import dev.brunofelix.moiseschallenge.core.presentation.design_system.spacing16

@Composable
fun SongItem(
    modifier: Modifier = Modifier,
    song: Song,
    isActionVisible: Boolean = true,
    onClick: () -> Unit = {},
    onAction: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(68.dp)
            .clickable(onClick = onClick)
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = song.coverUrl.toItunesImageSize(60),
            contentDescription = null,
            placeholder = ColorPainter(shimmerColorSecondary),
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(smallSpacing)),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(spacing16))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = song.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(extraSmallSpacing))
            Text(
                text = song.artist,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (isActionVisible) {
            IconButton(
                onClick = onAction,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_more),
                    tint = infoGrayColor,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun SongItemPreview() {
    AppTheme {
        SongItem(
            song = Song(
                id = 1,
                title = "Numb",
                artist = "Linkin Park"
            ),
            onAction = {},
            onClick = {}
        )
    }
}
