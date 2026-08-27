package dev.brunofelix.moiseschallenge.feature.player.presentation

import dev.brunofelix.moiseschallenge.core.domain.model.Song

internal data class PlayerUiState(
    val song: Song? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val totalDuration: Long = 0L,
    val isRepeatEnabled: Boolean = false,
    val isSheetVisible: Boolean = false
) {
    companion object {
        val preview = PlayerUiState(
            song = Song(
                id = 1,
                title = "In The End",
                artist = "Linkin Park",
            ),
            isPlaying = true,
            currentPosition = 86000L,
            totalDuration = 260000L,
            isRepeatEnabled = false
        )
    }
}