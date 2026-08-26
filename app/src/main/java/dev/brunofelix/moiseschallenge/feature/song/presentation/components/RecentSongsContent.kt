package dev.brunofelix.moiseschallenge.feature.song.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import dev.brunofelix.moiseschallenge.R
import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.presentation.components.AppStateMessage
import dev.brunofelix.moiseschallenge.core.presentation.components.SongItem
import dev.brunofelix.moiseschallenge.core.presentation.components.SongItemSkeleton
import dev.brunofelix.moiseschallenge.core.presentation.design_system.largeSpacing
import dev.brunofelix.moiseschallenge.core.presentation.design_system.spacing16
import dev.brunofelix.moiseschallenge.core.presentation.util.UiState

@Composable
internal fun RecentSongsContent(
    uiState: UiState<List<Song>>,
    onSongClick: (Song) -> Unit,
    onAlbumClick: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    if (uiState is UiState.Success) {
        val firstItemId = uiState.data.firstOrNull()?.id
        LaunchedEffect(firstItemId) {
            if (firstItemId != null) {
                listState.animateScrollToItem(0)
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is UiState.Initial, is UiState.Loading -> {
                RecentSongsList(modifier = Modifier.fillMaxSize()) {
                    items(10) { SongItemSkeleton() }
                }
            }
            is UiState.Empty -> {
                AppStateMessage(
                    icon = Icons.Rounded.LibraryMusic,
                    title = stringResource(R.string.empty_state_title),
                    subtitle = stringResource(R.string.empty_state_body),
                    modifier = Modifier.fillMaxSize(),
                )
            }
            is UiState.Error -> {
                AppStateMessage(
                    icon = Icons.Rounded.ErrorOutline,
                    title = stringResource(R.string.error_title),
                    subtitle = uiState.uiText.asString(LocalContext.current),
                    modifier = Modifier.fillMaxSize(),
                )
            }
            is UiState.Success -> {
                RecentSongsList(
                    modifier = Modifier.fillMaxSize(),
                    listState = listState
                ) {
                    items(
                        items = uiState.data,
                        key = { it.id }) { song ->
                            SongItem(
                                song = song,
                                onAction = { song.albumId?.let { id -> onAlbumClick(song) } },
                                onClick = { onSongClick(song) }
                            )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentSongsList(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(top = spacing16),
        state = listState,
        contentPadding = PaddingValues(bottom = largeSpacing),
        content = content
    )
}
