package dev.brunofelix.moiseschallenge.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.moiseschallenge.core.designsystem.R
import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.components.AppStateMessage
import dev.brunofelix.moiseschallenge.components.SongItem
import dev.brunofelix.moiseschallenge.components.SongItemSkeleton
import dev.brunofelix.moiseschallenge.theme.AppTheme
import dev.brunofelix.moiseschallenge.theme.largeSpacing
import dev.brunofelix.moiseschallenge.theme.spacing16
import dev.brunofelix.moiseschallenge.util.UiState

data class RecentSongsUiState(
    val uiState: UiState<List<Song>> = UiState.Loading,
    val listState: LazyListState = LazyListState()
)

sealed interface RecentSongsUiAction {
    data class OnSongClick(val song: Song) : RecentSongsUiAction
    data class OnAlbumClick(val song: Song) : RecentSongsUiAction
    data class OnDelete(val song: Song) : RecentSongsUiAction
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecentSongs(
    uiState: RecentSongsUiState,
    onAction: (RecentSongsUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when (uiState.uiState) {
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
                    subtitle = uiState.uiState.uiText.asString(LocalContext.current),
                    modifier = Modifier.fillMaxSize(),
                )
            }
            is UiState.Success -> {
                RecentSongsList(
                    modifier = Modifier.fillMaxSize(),
                    listState = uiState.listState
                ) {
                    items(
                        items = uiState.uiState.data,
                        key = { it.id }
                    ) { song ->
                        val dismissState = rememberSwipeToDismissBoxState()
                        LaunchedEffect(dismissState.currentValue) {
                            if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart || dismissState.currentValue == SwipeToDismissBoxValue.StartToEnd) {
                                onAction(RecentSongsUiAction.OnDelete(song))
                            }
                        }
                        SwipeToDismissBox(
                            state = dismissState,
                            backgroundContent = {
                                val color = when (dismissState.dismissDirection) {
                                    SwipeToDismissBoxValue.StartToEnd,
                                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
                                    SwipeToDismissBoxValue.Settled -> Color.Transparent
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color)
                                )
                            }
                        ) {
                            SongItem(
                                song = song,
                                onAction = { song.albumId?.let { id -> onAction(RecentSongsUiAction.OnAlbumClick(song)) } },
                                onClick = { onAction(RecentSongsUiAction.OnSongClick(song)) }
                            )
                        }
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

@Preview
@Composable
private fun RecentSongsLoadingPreview() {
    AppTheme {
        RecentSongs(
            uiState = RecentSongsUiState(uiState = UiState.Loading),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun RecentSongsEmptyPreview() {
    AppTheme {
        RecentSongs(
            uiState = RecentSongsUiState(uiState = UiState.Empty),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun RecentSongsSuccessPreview() {
    val mockSongs = listOf(
        Song(id = 1, title = "Numb", artist = "Linkin Park", albumId = 1L),
        Song(id = 2, title = "In the End", artist = "Linkin Park", albumId = 2L)
    )
    AppTheme {
        RecentSongs(
            uiState = RecentSongsUiState(uiState = UiState.Success(mockSongs)),
            onAction = {}
        )
    }
}
