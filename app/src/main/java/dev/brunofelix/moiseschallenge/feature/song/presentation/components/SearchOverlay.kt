package dev.brunofelix.moiseschallenge.feature.song.presentation.components

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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import dev.brunofelix.moiseschallenge.R
import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.presentation.components.AppSearchBar
import dev.brunofelix.moiseschallenge.core.presentation.components.AppStateMessage
import dev.brunofelix.moiseschallenge.core.presentation.components.SongItem
import dev.brunofelix.moiseschallenge.core.presentation.components.SongItemSkeleton
import dev.brunofelix.moiseschallenge.core.presentation.design_system.largeSpacing
import dev.brunofelix.moiseschallenge.core.presentation.design_system.smallSpacing
import dev.brunofelix.moiseschallenge.core.presentation.design_system.spacing16
import dev.brunofelix.moiseschallenge.core.presentation.util.UiState

@Composable
internal fun SearchOverlay(
    isVisible: Boolean,
    query: String,
    searchState: UiState<Unit>,
    searchResults: LazyPagingItems<Song>,
    focusRequester: FocusRequester,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit,
    onSongClick: (Song) -> Unit,
    onAlbumClick: (Song) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
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
                ) { onClose() },
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
                        query = query,
                        onQueryChange = onQueryChange,
                        modifier = Modifier.focusRequester(focusRequester)
                    )
                }
                when (searchState) {
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
                            subtitle = searchState.uiText.asString(LocalContext.current),
                            onRetry = onRetry,
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
                                        onAction = { song.albumId?.let { onAlbumClick(song) } },
                                        onClick = { onSongClick(song) }
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
