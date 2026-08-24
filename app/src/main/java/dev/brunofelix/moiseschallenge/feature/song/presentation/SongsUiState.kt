package dev.brunofelix.moiseschallenge.feature.song.presentation

data class SongsUiState(
    val query: String = "",
    val isLoading: Boolean = false
)