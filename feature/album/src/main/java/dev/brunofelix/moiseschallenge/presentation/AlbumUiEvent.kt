package dev.brunofelix.moiseschallenge.presentation

sealed interface AlbumUiEvent {
    data object NavigateBack : AlbumUiEvent
}
