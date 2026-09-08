package dev.brunofelix.moiseschallenge.use_case

import dev.brunofelix.moiseschallenge.model.Album
import dev.brunofelix.moiseschallenge.util.Resource

/**
 * Use case for fetching an album by its ID.
 */
fun interface GetAlbumByIdUseCase {
    /**
     * Fetches the complete album details, including all its tracks, from the remote API.
     *
     * @param id The unique identifier of the album.
     * @return A Resource containing the Album (which includes the List<Song>) or an error.
     */
    suspend operator fun invoke(id: Long): Resource<Album>
}
