package dev.brunofelix.moiseschallenge.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.brunofelix.moiseschallenge.local.entity.SongEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for interacting with the 'recent_songs' table.
 */
@Dao
interface SongDao {

    /**
     * Inserts a song into the 'recent_songs' table.
     * If the song already exists, it will be replaced.
     *
     * @param song The song to be inserted.
     * @return The ID of the inserted song.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(song: SongEntity): Long

    /**
     * Deletes a song from the 'recent_songs' table by its ID.
     *
     * @param id The ID of the song to delete.
     */
    @Query("DELETE FROM recent_songs WHERE id = :id")
    suspend fun delete(id: Long)

    /**
     * Retrieves the most recent 50 songs from the 'recent_songs' table.
     *
     * @return A flow emitting a list of the most recent 50 songs.
     */
    @Query("SELECT * FROM recent_songs ORDER BY playedAt DESC LIMIT 50")
    fun getRecentSongs(): Flow<List<SongEntity>>

    /**
     * Retrieves a specific song by its ID from the 'recent_songs' table.
     *
     * @param id The ID of the song to retrieve.
     * @return A flow emitting the requested song, or null if not found.
     */
    @Query("SELECT * FROM recent_songs WHERE id = :id LIMIT 1")
    fun getById(id: Long): Flow<SongEntity?>

    /**
     * Finds a specific song by its ID from the 'recent_songs' table.
     *
     * @param id The ID of the song to retrieve.
     * @return The requested song entity, or null if not found.
     */
    @Query("SELECT * FROM recent_songs WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): SongEntity?

    /**
     * Updates the last played timestamp for a specific song in the 'recent_songs' table.
     *
     * @param id The ID of the song to update.
     * @param lastPlayedAt The new last played timestamp.
     * @return The number of rows updated.
     */
    @Query("UPDATE recent_songs SET lastPlayedAt = :lastPlayedAt WHERE id = :id")
    suspend fun updateLastPlayedAt(id: Long, lastPlayedAt: Long)

    /**
     * Retrieves the last played song from the 'recent_songs' table.
     *
     * @return The last played song entity, or null if no song has been played.
     */
    @Query("SELECT * FROM recent_songs ORDER BY lastPlayedAt DESC LIMIT 1")
    fun getLastPlayedSong(): Flow<SongEntity?>
}