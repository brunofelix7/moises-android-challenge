package dev.brunofelix.moiseschallenge.feature.song.presentation

import dev.brunofelix.moiseschallenge.core.domain.model.Song

internal sealed interface SongUiAction {
    data class OnShowSearchBarChange(val show: Boolean) : SongUiAction
    data class OnQueryChange(val query: String) : SongUiAction
    data object OnRetrySearch : SongUiAction
    data class OnSongClick(val song: Song) : SongUiAction
    data class OnAlbumClick(val song: Song) : SongUiAction
    data class OnDeleteRecentSong(val song: Song) : SongUiAction
}
