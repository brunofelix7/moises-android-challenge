package dev.brunofelix.moiseschallenge.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_songs")
data class SongEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val title: String,
    val artistName: String,
    val coverUrl: String,
    val audioUrl: String,
    val durationMillis: Long,
    val albumId: Long,
    val playedAt: Long
)