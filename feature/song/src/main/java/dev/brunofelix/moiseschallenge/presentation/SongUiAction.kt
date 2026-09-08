package dev.brunofelix.moiseschallenge.presentation

import dev.brunofelix.moiseschallenge.model.Song

sealed interface SongUiAction {
    data class OnShowSearchBarChange(val show: Boolean) : SongUiAction
    data class OnQueryChange(val query: String) : SongUiAction
    data object OnRetrySearch : SongUiAction
    data class OnSongClick(val song: Song) : SongUiAction
    data class OnAlbumClick(val song: Song) : SongUiAction
    data class OnDeleteRecentSong(val song: Song) : SongUiAction
    data class OnSheetVisibleChange(val visible: Boolean) : SongUiAction
}
