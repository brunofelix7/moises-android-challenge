package dev.brunofelix.moiseschallenge.core.data.local.source

import dev.brunofelix.moiseschallenge.core.domain.model.Song
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for a local data source for songs.
 */
interface SongLocalDataSource {
    /**
     * Retrieves a flow of recent songs.
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
     * @param song The song to be saved.
     * @return The ID of the saved song.
     */
    suspend fun saveRecentSong(song: Song): Long
}