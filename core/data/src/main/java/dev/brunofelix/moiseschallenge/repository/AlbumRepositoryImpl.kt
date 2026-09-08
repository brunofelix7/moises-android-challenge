package dev.brunofelix.moiseschallenge.repository

import dev.brunofelix.moiseschallenge.model.Album
import dev.brunofelix.moiseschallenge.util.Resource
import dev.brunofelix.moiseschallenge.util.toResource
import dev.brunofelix.moiseschallenge.remote.source.SongRemoteDataSource
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val remoteDataSource: SongRemoteDataSource
) : AlbumRepository {

    override suspend fun getById(id: Long): Resource<Album> {
        return remoteDataSource.getAlbumDetails(id).toResource()
    }
}