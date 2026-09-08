package dev.brunofelix.moiseschallenge.presentation

import dev.brunofelix.moiseschallenge.model.Song

sealed interface AlbumUiAction {
    data object OnBack : AlbumUiAction
    data object OnLoadAlbum : AlbumUiAction
    data class OnTrackClick(val song: Song) : AlbumUiAction
}
