package dev.brunofelix.moiseschallenge.remote.mapper

import dev.brunofelix.moiseschallenge.model.Album
import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.remote.dto.AlbumDto

fun AlbumDto.toDomain(tracks: List<Song>): Album {
    return Album(
        id = collectionId ?: 0L,
        title = collectionName ?: "Unknown Album",
        artist = artistName ?: "Unknown Artist",
        coverUrl = coverUrl ?: "",
        tracks = tracks
    )
}