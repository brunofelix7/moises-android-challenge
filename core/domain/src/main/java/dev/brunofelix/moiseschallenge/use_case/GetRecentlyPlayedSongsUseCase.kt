package dev.brunofelix.moiseschallenge.use_case

import dev.brunofelix.moiseschallenge.model.Song
import kotlinx.coroutines.flow.Flow

/**
 * Use case for observing recently played songs.
 */
fun interface GetRecentlyPlayedSongsUseCase {
    /**
     * Observes the most recently played songs from the local database.
     * This ensures the Home screen always has data, even without internet.
     *
     * @return A flow of lists of recently played songs.
     */
    operator fun invoke(): Flow<List<Song>>
}
