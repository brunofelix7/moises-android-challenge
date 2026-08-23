package dev.brunofelix.moiseschallenge.core.domain.repository

import dev.brunofelix.moiseschallenge.core.domain.model.Album
import dev.brunofelix.moiseschallenge.core.domain.util.Resource

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