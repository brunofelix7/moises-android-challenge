package dev.brunofelix.moiseschallenge.core.domain.player

import dev.brunofelix.moiseschallenge.core.domain.model.Song
import kotlinx.coroutines.flow.Flow

/**
 * Core interface for controlling audio playback across the application.
 */
interface PlayerController {

    val playerState: Flow<PlayerState>
    val currentPosition: Flow<Long>
    val duration: Flow<Long>
    val repeatMode: Flow<PlayerRepeatMode>

    /**
     * Loads and plays a new song.
     */
    fun play(song: Song)

    /**
     * Resumes the currently loaded song if it was paused.
     */
    fun resume()

    /**
     * Pauses the current playback.
     */
    fun pause()

    /**
     * Stops playback and releases resources.
     */
    fun stop()

    /**
     * Seeks to a specific position in the audio.
     */
    fun seekTo(positionMs: Long)

    /**
     * Skips forward by a fixed amount of time (e.g., 5 seconds).
     */
    fun moveForward()

    /**
     * Skips backward by a fixed amount of time (e.g., 5 seconds).
     */
    fun moveBackward()

    /**
     * Toggles the repeat mode between ON and OFF.
     */
    fun toggleRepeatMode()

    /**
     * Releases the player and any associated resources.
     */
    fun release()
}