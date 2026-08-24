package dev.brunofelix.moiseschallenge.core.data.remote.mapper

import dev.brunofelix.moiseschallenge.core.data.remote.dto.SongDto
import dev.brunofelix.moiseschallenge.core.domain.model.Song

fun SongDto.toDomain(): Song {
    return Song(
        id = trackId ?: 0L,
        title = trackName ?: "Unknown Title",
        artist = artistName ?: "Unknown Artist",
        coverUrl = coverUrl ?: "",
        audioUrl = audioUrl ?: "",
        durationMillis = durationMillis ?: 0L,
        albumId = albumId ?: 0L
    )
}