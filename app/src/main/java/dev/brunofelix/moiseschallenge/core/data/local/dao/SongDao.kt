package dev.brunofelix.moiseschallenge.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.brunofelix.moiseschallenge.core.data.local.entity.SongEntity
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
     * Retrieves the most recent 50 songs from the 'recent_songs' table.
     *
     * @return A flow emitting a list of the most recent 50 songs.
     */
    @Query("SELECT * FROM recent_songs ORDER BY playedAt DESC LIMIT 50")
    fun getRecentSongs(): Flow<List<SongEntity>>
}