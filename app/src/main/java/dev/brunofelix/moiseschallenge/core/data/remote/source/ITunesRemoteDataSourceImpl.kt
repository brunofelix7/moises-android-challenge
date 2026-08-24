package dev.brunofelix.moiseschallenge.core.data.remote.source

import dev.brunofelix.moiseschallenge.core.data.remote.ITunesApi
import dev.brunofelix.moiseschallenge.core.data.remote.dto.AlbumDto
import dev.brunofelix.moiseschallenge.core.data.remote.dto.SongDto
import dev.brunofelix.moiseschallenge.core.data.remote.mapper.toDomain
import dev.brunofelix.moiseschallenge.core.data.util.BaseRemoteDataSource
import dev.brunofelix.moiseschallenge.core.domain.model.Album
import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.util.exception.RemoteException
import javax.inject.Inject

class ITunesRemoteDataSourceImpl @Inject constructor(
    api: ITunesApi
) : BaseRemoteDataSource<ITunesApi>(api), SongRemoteDataSource {

    override suspend fun search(term: String, limit: Int, offset: Int): Result<List<Song>> {
        return safeApiCall(
            call = { search(term = term, limit = limit, offset = offset) },
            transform = { dto ->
                dto.results.filterIsInstance<SongDto>().map { it.toDomain() }
            }
        )
    }

    override suspend fun getAlbumDetails(id: Long): Result<Album> {
        return safeApiCall(
            call = { lookup(id = id) },
            transform = { dto ->
                val albumDto = dto.results
                    .filterIsInstance<AlbumDto>()
                    .firstOrNull() ?: throw RemoteException.NotFound()
                val songs = dto.results.filterIsInstance<SongDto>().map { it.toDomain() }
                albumDto.toDomain(tracks = songs)
            }
        )
    }
}