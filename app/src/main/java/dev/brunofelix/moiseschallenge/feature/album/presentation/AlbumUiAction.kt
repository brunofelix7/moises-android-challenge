package dev.brunofelix.moiseschallenge.feature.album.presentation

import dev.brunofelix.moiseschallenge.core.domain.model.Song

internal sealed interface AlbumUiAction {
    data object OnBack : AlbumUiAction
    data object OnLoadAlbum : AlbumUiAction
    data class OnTrackClick(val song: Song) : AlbumUiAction
}
