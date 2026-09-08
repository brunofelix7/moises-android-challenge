package dev.brunofelix.moiseschallenge.use_case

import dev.brunofelix.moiseschallenge.model.Album
import dev.brunofelix.moiseschallenge.repository.AlbumRepository
import dev.brunofelix.moiseschallenge.util.Resource
import javax.inject.Inject

class GetAlbumByIdUseCaseImpl @Inject constructor(
    private val repository: AlbumRepository
) : GetAlbumByIdUseCase {

    override suspend fun invoke(id: Long): Resource<Album> {
        return repository.getById(id)
    }
}
