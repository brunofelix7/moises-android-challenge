package dev.brunofelix.moiseschallenge.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.brunofelix.moiseschallenge.core.data.local.dao.SongDao
import dev.brunofelix.moiseschallenge.core.data.local.entity.SongEntity

/**
 * Room database class for managing the 'recent_songs' table.
 * This database is used to store and retrieve recent songs.
 */
@Database(
    entities = [SongEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SongDatabase : RoomDatabase() {

    companion object {
        const val NAME = "songs_database"
    }

    abstract fun songDao(): SongDao
}