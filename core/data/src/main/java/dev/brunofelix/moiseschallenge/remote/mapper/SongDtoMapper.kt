package dev.brunofelix.moiseschallenge.remote.mapper

import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.remote.dto.SongDto

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