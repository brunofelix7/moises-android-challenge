package dev.brunofelix.moiseschallenge.feature.album.domain.use_case

import dev.brunofelix.moiseschallenge.core.domain.model.Album
import dev.brunofelix.moiseschallenge.core.domain.repository.AlbumRepository
import dev.brunofelix.moiseschallenge.core.domain.util.Resource
import javax.inject.Inject

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

class GetAlbumByIdUseCaseImpl @Inject constructor(
    private val repository: AlbumRepository
) : GetAlbumByIdUseCase {

    override suspend fun invoke(id: Long): Resource<Album> {
        return repository.getById(id)
    }
}