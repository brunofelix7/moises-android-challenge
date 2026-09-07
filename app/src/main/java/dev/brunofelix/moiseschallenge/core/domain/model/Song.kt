package dev.brunofelix.moiseschallenge.core.domain.model

data class Song(
    val id: Long = 0L,
    val title: String = "",
    val artist: String = "",
    val artistId: Long = 0L,
    val albumId: Long? = null,
    val albumName: String? = null,
    val coverUrl: String = "",
    val audioUrl: String = "",
    val durationMillis: Long = 0L,
    val lastPlayedAt: Long = 0L
)