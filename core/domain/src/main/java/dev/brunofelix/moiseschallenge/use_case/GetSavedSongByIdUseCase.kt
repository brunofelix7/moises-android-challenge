package dev.brunofelix.moiseschallenge.use_case

import dev.brunofelix.moiseschallenge.model.Song
import kotlinx.coroutines.flow.Flow

/**
 * Use case for observing a specific song by its ID.
 */
fun interface GetSavedSongByIdUseCase {
    /**
     * Observes a specific song from the local database by its ID.
     * This ensures the Player screen always has data, even without internet.
     *
     * @param id The unique identifier of the song.
     * @return A [Flow] emitting the [Song] if it exists, or null if it hasn't been saved yet.
     */
    operator fun invoke(id: Long): Flow<Song?>
}
