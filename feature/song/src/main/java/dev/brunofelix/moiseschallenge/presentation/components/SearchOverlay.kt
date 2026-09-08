package dev.brunofelix.moiseschallenge.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import dev.brunofelix.moiseschallenge.core.designsystem.R
import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.components.AppSearchBar
import dev.brunofelix.moiseschallenge.components.AppStateMessage
import dev.brunofelix.moiseschallenge.components.SongItem
import dev.brunofelix.moiseschallenge.components.SongItemSkeleton
import dev.brunofelix.moiseschallenge.theme.AppTheme
import dev.brunofelix.moiseschallenge.theme.largeSpacing
import dev.brunofelix.moiseschallenge.theme.smallSpacing
import dev.brunofelix.moiseschallenge.theme.spacing16
import dev.brunofelix.moiseschallenge.util.UiState
import kotlinx.coroutines.flow.flowOf

data class SearchOverlayUiState(
    val isVisible: Boolean = false,
    val query: String = "",
    val searchState: UiState<Unit> = UiState.Initial,
    val focusRequester: FocusRequester = FocusRequester()
)

sealed interface SearchOverlayUiAction {
    data class OnQueryChange(val query: String) : SearchOverlayUiAction
    data object OnClose : SearchOverlayUiAction
    data class OnSongClick(val song: Song) : SearchOverlayUiAction
    data class OnAlbumClick(val song: Song) : SearchOverlayUiAction
    data object OnRetry : SearchOverlayUiAction
}

@Composable
internal fun SearchOverlay(
    uiState: SearchOverlayUiState,
    searchResults: LazyPagingItems<Song>,
    onAction: (SearchOverlayUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = uiState.isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onAction(SearchOverlayUiAction.OnClose) },
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = largeSpacing, vertical = smallSpacing)
                        .clickable(enabled = false) {}
                ) {
                    AppSearchBar(
                        query = uiState.query,
                        onQueryChange = { query -> onAction(SearchOverlayUiAction.OnQueryChange(query)) },
                        modifier = Modifier.focusRequester(uiState.focusRequester)
                    )
                }
                when (uiState.searchState) {
                    is UiState.Loading -> {
                        SearchList(modifier = Modifier.fillMaxSize()) {
                            items(10) { SongItemSkeleton() }
                        }
                    }
                    is UiState.Empty -> {
                        AppStateMessage(
                            icon = Icons.Rounded.SearchOff,
                            title = stringResource(R.string.search_no_results_title),
                            subtitle = stringResource(R.string.search_no_results_body),
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    is UiState.Error -> {
                        AppStateMessage(
                            icon = Icons.Rounded.ErrorOutline,
                            title = stringResource(R.string.error_title),
                            subtitle = uiState.searchState.uiText.asString(LocalContext.current),
                            onRetry = { onAction(SearchOverlayUiAction.OnRetry) },
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    is UiState.Success -> {
                        SearchList(modifier = Modifier.fillMaxSize()) {
                            items(
                                count = searchResults.itemCount,
                                key = searchResults.itemKey { song -> song.id }
                            ) { index ->
                                val song = searchResults[index]
                                if (song != null) {
                                    SongItem(
                                        song = song,
                                        onAction = { song.albumId?.let { onAction(SearchOverlayUiAction.OnAlbumClick(song)) } },
                                        onClick = { onAction(SearchOverlayUiAction.OnSongClick(song)) }
                                    )
                                }
                            }
                        }
                    }
                    is UiState.Initial -> Unit
                }
            }
        }
    }
}

@Composable
private fun SearchList(
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(top = spacing16),
        contentPadding = PaddingValues(bottom = largeSpacing),
        content = content
    )
}

@Preview
@Composable
private fun SearchOverlayPreview() {
    val mockSongs = listOf(
        Song(id = 1, title = "Numb", artist = "Linkin Park", albumId = 1L),
        Song(id = 2, title = "In the End", artist = "Linkin Park", albumId = 2L)
    )
    AppTheme {
        SearchOverlay(
            uiState = SearchOverlayUiState(
                isVisible = true,
                query = "Linkin",
                searchState = UiState.Success(Unit)
            ),
            searchResults = flowOf(PagingData.from(mockSongs)).collectAsLazyPagingItems(),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun SearchOverlayEmptyPreview() {
    AppTheme {
        SearchOverlay(
            uiState = SearchOverlayUiState(
                isVisible = true,
                query = "Unknown",
                searchState = UiState.Empty
            ),
            searchResults = flowOf(PagingData.empty<Song>()).collectAsLazyPagingItems(),
            onAction = {}
        )
    }
}
