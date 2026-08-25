package dev.brunofelix.moiseschallenge.feature.song.presentation

import dev.brunofelix.moiseschallenge.core.presentation.util.UiState

data class SongUiState(
    val query: String = "",
    val searchState: UiState<Unit> = UiState.Initial
)