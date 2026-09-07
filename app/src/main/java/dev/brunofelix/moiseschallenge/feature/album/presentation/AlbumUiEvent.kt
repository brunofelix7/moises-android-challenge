package dev.brunofelix.moiseschallenge.feature.album.presentation

sealed interface AlbumUiEvent {
    data object NavigateBack : AlbumUiEvent
}
