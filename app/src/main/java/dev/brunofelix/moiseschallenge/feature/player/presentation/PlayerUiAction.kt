package dev.brunofelix.moiseschallenge.feature.player.presentation

sealed interface PlayerUiAction {
    data object OnPlayPause : PlayerUiAction
    data object OnPrevious : PlayerUiAction
    data object OnNext : PlayerUiAction
    data class OnSeek(val position: Float) : PlayerUiAction
    data object OnToggleRepeat : PlayerUiAction
    data object OnBack : PlayerUiAction
    data object OnActionClick : PlayerUiAction
    data object OnDismissSheet : PlayerUiAction
    data object OnViewAlbumClick : PlayerUiAction
}
