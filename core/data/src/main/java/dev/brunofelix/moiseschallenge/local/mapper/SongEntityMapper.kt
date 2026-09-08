package dev.brunofelix.moiseschallenge.local.mapper

import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.local.entity.SongEntity

fun SongEntity.toDomain(): Song {
    return Song(
        id = id,
        title = title,
        artist = artistName,
        coverUrl = coverUrl,
        audioUrl = audioUrl,
        durationMillis = durationMillis,
        albumId = albumId,
        lastPlayedAt = lastPlayedAt
    )
}

fun Song.toEntity(): SongEntity {
    return SongEntity(
        id = id,
        title = title,
        artistName = artist,
        coverUrl = coverUrl,
        audioUrl = audioUrl,
        durationMillis = durationMillis,
        albumId = albumId ?: 0L,
        playedAt = System.currentTimeMillis(),
        lastPlayedAt = lastPlayedAt
    )
}