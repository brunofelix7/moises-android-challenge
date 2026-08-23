package dev.brunofelix.moiseschallenge.core.domain.repository

import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for songs.
 */
interface SongRepository {

    /**
     * Searches for songs. In an offline-first app, this could hit the API
     * or even fallback to searching local records if the network fails.
     *
     * @param query The search query.
     */
    suspend fun search(query: String): Resource<List<Song>>

    /**
     * Saves a song to the local database and marks it as recently played.
     * Call this whenever the user plays a song.
     *
     * @param song The song to save and mark as recently played.
     */
    suspend fun saveRecent(song: Song)

    /**
     * Observes a specific song from the local database by its ID.
     * Perfect for the Player screen to maintain Single Source of Truth.
     *
     * @param id The unique identifier of the song.
     * @return A [Flow] emitting the [Song] if it exists, or null if it hasn't been saved yet.
     */
    fun observeById(id: Long): Flow<Song?>

    /**
     * Observes the most recently played songs from the local database.
     * This ensures the Home screen always has data, even without internet.
     *
     * @return A flow of lists of recently played songs.
     */
    fun observeRecentlyPlayed(): Flow<List<Song>>
}