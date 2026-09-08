package dev.brunofelix.moiseschallenge.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.brunofelix.moiseschallenge.core.designsystem.R
import dev.brunofelix.moiseschallenge.theme.AppTheme
import dev.brunofelix.moiseschallenge.theme.extraSmallSpacing
import dev.brunofelix.moiseschallenge.theme.sheetBackgroundColor
import dev.brunofelix.moiseschallenge.theme.sliderDarkGrayColor
import dev.brunofelix.moiseschallenge.theme.spacing16
import dev.brunofelix.moiseschallenge.theme.spacing18
import dev.brunofelix.moiseschallenge.theme.spacing24

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongActionSheet(
    modifier: Modifier = Modifier,
    songName: String = stringResource(R.string.song_name),
    artistName: String = stringResource(R.string.artist_name),
    onDismiss: () -> Unit,
    onViewAlbumClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        containerColor = sheetBackgroundColor,
        shape = RoundedCornerShape(topStart = spacing16, topEnd = spacing16),
        dragHandle = { ActionSheetDragHandle() }
    ) {
        SongActionContent(
            songName = songName,
            artistName = artistName,
            onViewAlbumClick = onViewAlbumClick
        )
    }
}

@Composable
private fun SongActionContent(
    modifier: Modifier = Modifier,
    songName: String,
    artistName: String,
    onViewAlbumClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 41.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = songName,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = spacing16)
        )
        Spacer(modifier = Modifier.height(extraSmallSpacing))
        Text(
            text = artistName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = spacing16)
        )
        Spacer(modifier = Modifier.height(spacing18))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { onViewAlbumClick() }
                .padding(horizontal = spacing24, vertical = spacing16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_setlist),
                contentDescription = stringResource(R.string.cd_view_album),
                tint = Color.White,
                modifier = Modifier.size(spacing24)
            )
            Spacer(modifier = Modifier.width(spacing16))
            Text(
                text = stringResource(R.string.view_album),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ActionSheetDragHandle() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = extraSmallSpacing, bottom = spacing16),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(56.dp)
                .height(5.dp)
                .background(sliderDarkGrayColor, CircleShape)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2C2C2C)
@Composable
private fun SongActionSheetPreview() {
    AppTheme {
        SongActionContent(
            songName = stringResource(R.string.song_name),
            artistName = stringResource(R.string.artist_name),
            onViewAlbumClick = {}
        )
    }
}