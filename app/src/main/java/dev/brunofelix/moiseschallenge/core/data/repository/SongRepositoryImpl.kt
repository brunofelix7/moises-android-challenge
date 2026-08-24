package dev.brunofelix.moiseschallenge.core.data.repository

import dev.brunofelix.moiseschallenge.core.data.local.source.SongLocalDataSource
import dev.brunofelix.moiseschallenge.core.data.remote.source.SongRemoteDataSource
import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.repository.SongRepository
import dev.brunofelix.moiseschallenge.core.domain.util.Resource
import dev.brunofelix.moiseschallenge.core.domain.util.toResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val remoteDataSource: SongRemoteDataSource,
    private val localDataSource: SongLocalDataSource
) : SongRepository {

    override suspend fun search(term: String, limit: Int, offset: Int): Resource<List<Song>> {
        return remoteDataSource.search(term, limit, offset).toResource()
    }

    override suspend fun saveRecent(song: Song): Long {
        return localDataSource.saveRecentSong(song)
    }

    override fun observeById(id: Long): Flow<Song?> {
        return localDataSource.observeSongById(id)
    }

    override fun observeRecentlyPlayed(): Flow<List<Song>> {
        return localDataSource.getRecentSongs()
    }
}