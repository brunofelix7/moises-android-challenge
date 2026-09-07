package dev.brunofelix.moiseschallenge.feature.song.presentation

import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.presentation.util.UiState

data class SongUiState(
    val query: String = "",
    val searchState: UiState<Unit> = UiState.Initial,
    val recentSongsState: UiState<List<Song>> = UiState.Loading,
    val showSearchBar: Boolean = false,
    val isSheetVisible: Boolean = false,
    val albumId: Long = 0L,
    val selectedSong: Song = Song()
)
