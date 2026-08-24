package dev.brunofelix.moiseschallenge.core.data.repository

import dev.brunofelix.moiseschallenge.core.data.remote.source.SongRemoteDataSource
import dev.brunofelix.moiseschallenge.core.domain.model.Album
import dev.brunofelix.moiseschallenge.core.domain.repository.AlbumRepository
import dev.brunofelix.moiseschallenge.core.domain.util.Resource
import dev.brunofelix.moiseschallenge.core.domain.util.toResource
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val remoteDataSource: SongRemoteDataSource
) : AlbumRepository {

    override suspend fun getById(id: Long): Resource<Album> {
        return remoteDataSource.getAlbumDetails(id).toResource()
    }
}