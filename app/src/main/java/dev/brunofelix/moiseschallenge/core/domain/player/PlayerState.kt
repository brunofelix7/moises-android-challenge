package dev.brunofelix.moiseschallenge.core.domain.player

/**
 * Represents the current playback state of the audio player.
 */
sealed interface PlayerState {
    /**
     * The player is idle, meaning no media is currently loaded or playing.
     */
    data object Idle : PlayerState

    /**
     * The player is buffering data from the network.
     */
    data object Buffering : PlayerState

    /**
     * The media is currently playing.
     */
    data object Playing : PlayerState

    /**
     * The media is paused but ready to be resumed.
     */
    data object Paused : PlayerState

    /**
     * An error occurred during playback (e.g., network loss or decoding failure).
     *
     * @property message A descriptive error message.
     */
    data class Error(val message: String) : PlayerState
}