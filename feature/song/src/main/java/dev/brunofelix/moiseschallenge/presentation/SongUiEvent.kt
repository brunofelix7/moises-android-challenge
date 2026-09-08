package dev.brunofelix.moiseschallenge.presentation

sealed interface SongUiEvent {
    data object ScrollToTop : SongUiEvent
}