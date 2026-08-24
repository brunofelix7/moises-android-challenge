package dev.brunofelix.moiseschallenge.feature.song.presentation

data class SongUiState(
    val query: String = "",
    val isLoading: Boolean = false
)