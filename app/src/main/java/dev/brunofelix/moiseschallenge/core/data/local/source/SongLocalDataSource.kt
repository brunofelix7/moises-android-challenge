package dev.brunofelix.moiseschallenge.core.data.local.source

import dev.brunofelix.moiseschallenge.core.domain.model.Song
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for a local data source for songs.
 */
interface SongLocalDataSource {
    /**
     * Retrieves a flow of recent songs.
     *
     * @return A flow emitting a list of recent songs.
     */
    fun getRecentSongs(): Flow<List<Song>>

    /**
     * Retrieves a flow of a specific song by its ID.
     *
     * @param id The ID of the song to retrieve.
     * @return A flow emitting the requested song, or null if not found.
     */
    fun observeSongById(id: Long): Flow<Song?>

    /**
     * Saves a recent song.
     *
     * @param song The song to be saved.
     * @return The ID of the saved song.
     */
    suspend fun saveRecentSong(song: Song): Long

    /**
     * Deletes a recent song by its ID.
     *
     * @param id The ID of the song to delete.
     */
    suspend fun deleteRecentSong(id: Long)

    /**
     * Updates the last played timestamp for a specific song.
     *
     * @param id The ID of the song to update.
     * @param lastPlayedAt The new last played timestamp.
     */
    suspend fun updateLastPlayedAt(id: Long, lastPlayedAt: Long)

    /**
     * Retrieves the last played song.
     *
     * @return The last played song, or null if no song has been played.
     */
    fun getLastPlayedSong(): Flow<Song?>
}