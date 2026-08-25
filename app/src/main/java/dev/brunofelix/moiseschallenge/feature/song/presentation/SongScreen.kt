package dev.brunofelix.moiseschallenge.feature.song.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme
import dev.brunofelix.moiseschallenge.core.presentation.navigation.Route
import dev.brunofelix.moiseschallenge.core.presentation.util.UiState
import dev.brunofelix.moiseschallenge.feature.song.presentation.components.RecentSongsContent
import dev.brunofelix.moiseschallenge.feature.song.presentation.components.SearchOverlay
import dev.brunofelix.moiseschallenge.feature.song.presentation.components.SongActionSheet
import dev.brunofelix.moiseschallenge.feature.song.presentation.components.SongTopBar
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun SongRoute(
    onNavigate: (Route) -> Unit,
    viewModel: SongViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val recentSongsState by viewModel.recentlyPlayedSongs.collectAsStateWithLifecycle()
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()
    var showSearchBar by remember { mutableStateOf(false) }
    var isSheetVisible by remember { mutableStateOf(false) }
    var albumId by remember { mutableLongStateOf(0L) }
    var selectedSong by remember { mutableStateOf(Song()) }

    SongScreen(
        uiState = uiState,
        recentSongsState = recentSongsState,
        searchResults = searchResults,
        showSearchBar = showSearchBar,
        onShowSearchBarChange = { showSearchBar = it },
        onQueryChange = viewModel::onQueryChange,
        onRetrySearch = viewModel::onRetrySearch,
        onSongClick = { song ->
            viewModel.onSongPlayed(song)
            onNavigate(Route.Player(song.id))
        },
        onAlbumClick = { song ->
            albumId = song.albumId ?: 0L
            selectedSong = song
            isSheetVisible = true
        }
    )

    if (isSheetVisible) {
        SongActionSheet(
            songName = selectedSong.title,
            artistName = selectedSong.artist,
            onDismiss = {
                isSheetVisible = false
            },
            onViewAlbumClick = {
                isSheetVisible = false
                onNavigate(Route.Album(albumId))
            }
        )
    }
}

@Composable
internal fun SongScreen(
    modifier: Modifier = Modifier,
    uiState: SongUiState,
    recentSongsState: UiState<List<Song>>,
    searchResults: LazyPagingItems<Song>,
    showSearchBar: Boolean,
    onShowSearchBarChange: (Boolean) -> Unit = {},
    onQueryChange: (String) -> Unit = {},
    onRetrySearch: () -> Unit = {},
    onSongClick: (Song) -> Unit = {},
    onAlbumClick: (Song) -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val closeSearch = {
        onShowSearchBarChange(false)
        onQueryChange("")
    }

    LaunchedEffect(showSearchBar) {
        if (showSearchBar) {
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
                showSearchBar = showSearchBar,
                onShowSearchBarChange = onShowSearchBarChange,
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
                RecentSongsContent(
                    uiState = recentSongsState,
                    onSongClick = { song ->
                        closeSearch()
                        onSongClick(song)
                    },
                    onAlbumClick = { song ->
                        onAlbumClick(song)
                    }
                )
                SearchOverlay(
                    isVisible = showSearchBar,
                    query = uiState.query,
                    searchState = uiState.searchState,
                    searchResults = searchResults,
                    focusRequester = focusRequester,
                    onQueryChange = onQueryChange,
                    onClose = closeSearch,
                    onSongClick = { song ->
                        closeSearch()
                        onSongClick(song)
                    },
                    onAlbumClick = { song ->
                        onAlbumClick(song)
                    },
                    onRetry = onRetrySearch,
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
            uiState = SongUiState(),
            recentSongsState = UiState.Empty,
            searchResults = flowOf(PagingData.empty<Song>()).collectAsLazyPagingItems(),
            showSearchBar = false
        )
    }
}

@Preview
@Composable
private fun LoadingPreview() {
    AppTheme {
        SongScreen(
            uiState = SongUiState(),
            recentSongsState = UiState.Loading,
            searchResults = flowOf(PagingData.empty<Song>()).collectAsLazyPagingItems(),
            showSearchBar = false
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
            uiState = SongUiState(),
            recentSongsState = UiState.Success(mockSongs),
            searchResults = flowOf(PagingData.empty<Song>()).collectAsLazyPagingItems(),
            showSearchBar = false
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
            uiState = SongUiState(query = "Linkin", searchState = UiState.Success(Unit)),
            recentSongsState = UiState.Success(mockSongs),
            searchResults = flowOf(PagingData.from(mockSongs)).collectAsLazyPagingItems(),
            showSearchBar = true
        )
    }
}
