package dev.brunofelix.moiseschallenge.remote.source

import dev.brunofelix.moiseschallenge.model.Album
import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.util.exception.RemoteException
import dev.brunofelix.moiseschallenge.remote.ITunesApi
import dev.brunofelix.moiseschallenge.remote.dto.AlbumDto
import dev.brunofelix.moiseschallenge.remote.dto.SongDto
import dev.brunofelix.moiseschallenge.remote.mapper.toDomain
import dev.brunofelix.moiseschallenge.util.BaseRemoteDataSource
import javax.inject.Inject

class ITunesRemoteDataSourceImpl @Inject constructor(
    api: ITunesApi
) : BaseRemoteDataSource<ITunesApi>(api), SongRemoteDataSource {

    override suspend fun search(term: String, limit: Int): Result<List<Song>> {
        return safeApiCall(
            call = { search(term = term, limit = limit) },
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