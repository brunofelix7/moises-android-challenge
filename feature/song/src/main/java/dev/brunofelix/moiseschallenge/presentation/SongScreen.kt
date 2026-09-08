package dev.brunofelix.moiseschallenge.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import dev.brunofelix.moiseschallenge.components.SongActionSheet
import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.navigation.Route
import dev.brunofelix.moiseschallenge.presentation.components.RecentSongs
import dev.brunofelix.moiseschallenge.presentation.components.RecentSongsUiAction
import dev.brunofelix.moiseschallenge.presentation.components.RecentSongsUiState
import dev.brunofelix.moiseschallenge.presentation.components.SearchOverlay
import dev.brunofelix.moiseschallenge.presentation.components.SearchOverlayUiAction
import dev.brunofelix.moiseschallenge.presentation.components.SearchOverlayUiState
import dev.brunofelix.moiseschallenge.presentation.components.SongTopBar
import dev.brunofelix.moiseschallenge.theme.AppTheme
import dev.brunofelix.moiseschallenge.util.ObserveAsEvents
import dev.brunofelix.moiseschallenge.util.UiState
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun SongRoute(
    onNavigate: (Route) -> Unit,
    viewModel: SongViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()
    val onAction = viewModel::onAction
    val listState = rememberLazyListState()

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            is SongUiEvent.ScrollToTop -> {
                listState.animateScrollToItem(0)
            }
        }
    }

    SongScreen(
        uiState = uiState,
        searchResults = searchResults,
        listState = listState,
        onAction = onAction
    )

    AnimatedVisibility(
        visible = uiState.isSheetVisible
    ) {
        SongActionSheet(
            songName = uiState.selectedSong.title,
            artistName = uiState.selectedSong.artist,
            onDismiss = {
                onAction(SongUiAction.OnSheetVisibleChange(false))
            },
            onViewAlbumClick = {
                onAction(SongUiAction.OnSheetVisibleChange(false))
                onNavigate(Route.Album(uiState.albumId))
            }
        )
    }
}

@Composable
internal fun SongScreen(
    uiState: SongUiState,
    searchResults: LazyPagingItems<Song>,
    onAction: (SongUiAction) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState()
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val closeSearch = {
        onAction(SongUiAction.OnShowSearchBarChange(false))
        onAction(SongUiAction.OnQueryChange(""))
    }

    LaunchedEffect(uiState.showSearchBar) {
        if (uiState.showSearchBar) {
            focusRequester.requestFocus()
            keyboardController?.show()
        } else {
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            SongTopBar(
                showSearchBar = uiState.showSearchBar,
                onShowSearchBarChange = { show -> onAction(SongUiAction.OnShowSearchBarChange(show)) },
                onCancelSearch = closeSearch
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
            ) {
                RecentSongs(
                    uiState = RecentSongsUiState(
                        uiState = uiState.recentSongsState,
                        listState = listState
                    ),
                    onAction = { action ->
                        when (action) {
                            is RecentSongsUiAction.OnSongClick -> {
                                closeSearch()
                                onAction(SongUiAction.OnSongClick(action.song))
                            }
                            is RecentSongsUiAction.OnAlbumClick -> {
                                onAction(SongUiAction.OnAlbumClick(action.song))
                            }
                            is RecentSongsUiAction.OnDelete -> {
                                onAction(SongUiAction.OnDeleteRecentSong(action.song))
                            }
                        }
                    }
                )
                SearchOverlay(
                    uiState = SearchOverlayUiState(
                        isVisible = uiState.showSearchBar,
                        query = uiState.query,
                        searchState = uiState.searchState,
                        focusRequester = focusRequester
                    ),
                    searchResults = searchResults,
                    onAction = { action ->
                        when (action) {
                            is SearchOverlayUiAction.OnQueryChange -> {
                                onAction(SongUiAction.OnQueryChange(action.query))
                            }
                            is SearchOverlayUiAction.OnClose -> {
                                closeSearch()
                            }
                            is SearchOverlayUiAction.OnSongClick -> {
                                closeSearch()
                                onAction(SongUiAction.OnSongClick(action.song))
                            }
                            is SearchOverlayUiAction.OnAlbumClick -> {
                                onAction(SongUiAction.OnAlbumClick(action.song))
                            }
                            is SearchOverlayUiAction.OnRetry -> {
                                onAction(SongUiAction.OnRetrySearch)
                            }
                        }
                    },
                    modifier = Modifier.zIndex(1f)
                )
            }
        }
    )
}

@Preview
@Composable
private fun EmptyPreview() {
    AppTheme {
        SongScreen(
            uiState = SongUiState(recentSongsState = UiState.Empty),
            searchResults = flowOf(PagingData.empty<Song>()).collectAsLazyPagingItems(),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun LoadingPreview() {
    AppTheme {
        SongScreen(
            uiState = SongUiState(recentSongsState = UiState.Loading),
            searchResults = flowOf(PagingData.empty<Song>()).collectAsLazyPagingItems(),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun RecentSongsPreview() {
    val mockSongs = listOf(
        Song(id = 1, title = "Numb", artist = "Linkin Park"),
        Song(id = 2, title = "In the End", artist = "Linkin Park"),
    )
    AppTheme {
        SongScreen(
            uiState = SongUiState(recentSongsState = UiState.Success(mockSongs)),
            searchResults = flowOf(PagingData.empty<Song>()).collectAsLazyPagingItems(),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun SearchPreview() {
    val mockSongs = listOf(
        Song(id = 1, title = "Numb", artist = "Linkin Park"),
        Song(id = 2, title = "In the End", artist = "Linkin Park")
    )
    AppTheme {
        SongScreen(
            uiState = SongUiState(
                query = "Linkin",
                searchState = UiState.Success(Unit),
                recentSongsState = UiState.Success(mockSongs),
                showSearchBar = true
            ),
            searchResults = flowOf(PagingData.from(mockSongs)).collectAsLazyPagingItems(),
            onAction = {}
        )
    }
}
