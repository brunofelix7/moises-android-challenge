package dev.brunofelix.moiseschallenge.core.data.remote.mapper

import dev.brunofelix.moiseschallenge.core.data.remote.dto.AlbumDto
import dev.brunofelix.moiseschallenge.core.domain.model.Album
import dev.brunofelix.moiseschallenge.core.domain.model.Song

fun AlbumDto.toDomain(tracks: List<Song>): Album {
    return Album(
        id = collectionId ?: 0L,
        title = collectionName ?: "Unknown Album",
        artist = artistName ?: "Unknown Artist",
        coverUrl = coverUrl ?: "",
        tracks = tracks
    )
}