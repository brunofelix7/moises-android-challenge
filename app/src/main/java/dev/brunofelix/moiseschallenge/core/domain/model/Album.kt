package dev.brunofelix.moiseschallenge.core.domain.model

data class Album(
    val id: Long = 0L,
    val title: String = "",
    val artist: String = "",
    val coverUrl: String = "",
    val tracks: List<Song> = emptyList()
)