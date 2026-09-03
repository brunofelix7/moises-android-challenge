package dev.brunofelix.moiseschallenge.core.data.local.mapper

import dev.brunofelix.moiseschallenge.core.data.local.entity.SongEntity
import dev.brunofelix.moiseschallenge.core.domain.model.Song

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