package dev.brunofelix.moiseschallenge.feature.album.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.brunofelix.moiseschallenge.R
import dev.brunofelix.moiseschallenge.core.domain.model.Album
import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.presentation.components.AppStateMessage
import dev.brunofelix.moiseschallenge.core.presentation.components.AppTopBar
import dev.brunofelix.moiseschallenge.core.presentation.components.SongItem
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme
import dev.brunofelix.moiseschallenge.core.presentation.design_system.extraLargeSpacing
import dev.brunofelix.moiseschallenge.core.presentation.navigation.Route
import dev.brunofelix.moiseschallenge.core.presentation.util.ObserveAsEvents
import dev.brunofelix.moiseschallenge.core.presentation.util.UiState
import dev.brunofelix.moiseschallenge.core.presentation.util.UiText
import dev.brunofelix.moiseschallenge.feature.album.presentation.components.AlbumHeader
import dev.brunofelix.moiseschallenge.feature.album.presentation.components.AlbumSkeleton

@Composable
internal fun AlbumRoute(
    albumId: Long,
    onReplace: (Route) -> Unit,
    onBack: () -> Unit,
    viewModel: AlbumViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    BackHandler(enabled = true) {
        onAction(AlbumUiAction.OnBack)
    }

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            AlbumUiEvent.NavigateBack -> onBack()
        }
    }

    LaunchedEffect(albumId) {
        viewModel.loadAlbum(albumId)
    }

    AlbumScreen(
        uiState = uiState,
        onAction = onAction
    )
}

@Composable
internal fun AlbumScreen(
    uiState: UiState<Album>,
    onAction: (AlbumUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val title = (uiState as? UiState.Success)?.data?.title ?: ""

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                title = title,
                titleStyle = MaterialTheme.typography.titleSmall,
                onBack = { onAction(AlbumUiAction.OnBack) }
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is UiState.Initial,
            is UiState.Loading -> {
                AlbumSkeleton(
                    modifier = Modifier.padding(innerPadding)
                )
            }
            is UiState.Success -> {
                AlbumContent(
                    album = uiState.data,
                    onTrackClick = { song -> onAction(AlbumUiAction.OnTrackClick(song)) },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            is UiState.Empty -> {
                AppStateMessage(
                    icon = Icons.Rounded.LibraryMusic,
                    title = stringResource(R.string.album_no_album_title),
                    subtitle = stringResource(R.string.album_no_album_body),
                    modifier = Modifier.padding(innerPadding)
                )
            }
            is UiState.Error -> {
                AppStateMessage(
                    icon = Icons.Rounded.ErrorOutline,
                    title = stringResource(R.string.error_title),
                    subtitle = uiState.uiText.asString(LocalContext.current),
                    modifier = Modifier.padding(innerPadding),
                    onRetry = { onAction(AlbumUiAction.OnLoadAlbum) }
                )
            }
        }
    }
}

@Composable
private fun AlbumContent(
    album: Album,
    onTrackClick: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = extraLargeSpacing)
    ) {
        item {
            AlbumHeader(
                album = album,
                modifier = Modifier.padding(vertical = extraLargeSpacing)
            )
        }
        items(album.tracks) { track ->
            SongItem(
                song = track,
                itemHeight = 60.dp,
                imageSize = 44.dp,
                isActionVisible = false,
                onClick = { onTrackClick(track) }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun LoadingPreview() {
    AppTheme {
        AlbumScreen(
            uiState = UiState.Loading,
            onAction = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun SuccessPreview() {
    AppTheme {
        AlbumScreen(
            uiState = UiState.Success(
                data = Album(
                    id = 1L,
                    title = "Meteora",
                    artist = "Linkin Park",
                    tracks = listOf(
                        Song(1, "Numb", "Linkin Park"),
                        Song(2, "Lying From You", "Linkin Park"),
                        Song(3, "Somewhere I Belong", "Linkin Park")
                    )
                )
            ),
            onAction = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun EmptyPreview() {
    AppTheme {
        AlbumScreen(
            uiState = UiState.Empty,
            onAction = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun ErrorPreview() {
    AppTheme {
        AlbumScreen(
            uiState = UiState.Error(UiText.DynamicString("Could not load album details")),
            onAction = {}
        )
    }
}
