package dev.brunofelix.moiseschallenge.feature.song.presentation

sealed interface SongUiEvent {
    data object ScrollToTop : SongUiEvent
}