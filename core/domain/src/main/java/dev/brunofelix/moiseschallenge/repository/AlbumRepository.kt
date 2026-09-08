package dev.brunofelix.moiseschallenge.repository

import dev.brunofelix.moiseschallenge.model.Album
import dev.brunofelix.moiseschallenge.util.Resource

/**
 * Repository interface for albums.
 */
interface AlbumRepository {

    /**
     * Fetches the complete album details, including all its tracks, from the remote API.
     *
     * @param id The unique identifier of the album.
     * @return A Resource containing the Album (which includes the List<Song>) or an error.
     */
    suspend fun getById(id: Long): Resource<Album>
}