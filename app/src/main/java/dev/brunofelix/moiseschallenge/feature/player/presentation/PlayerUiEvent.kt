package dev.brunofelix.moiseschallenge.feature.player.presentation

sealed interface PlayerUiEvent {
    data object NavigateBack : PlayerUiEvent
    data class NavigateToAlbum(val albumId: Long) : PlayerUiEvent
}
