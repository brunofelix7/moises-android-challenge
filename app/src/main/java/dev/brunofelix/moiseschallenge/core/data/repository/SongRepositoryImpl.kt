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

    override suspend fun search(term: String, limit: Int): Resource<List<Song>> {
        return remoteDataSource.search(term, limit).toResource()
    }

    override suspend fun saveRecent(song: Song): Long {
        return localDataSource.saveRecentSong(song)
    }

    override suspend fun deleteRecent(id: Long) {
        localDataSource.deleteRecentSong(id)
    }

    override fun observeById(id: Long): Flow<Song?> {
        return localDataSource.observeSongById(id)
    }

    override fun observeRecentlyPlayed(): Flow<List<Song>> {
        return localDataSource.getRecentSongs()
    }

    override suspend fun updateLastPlayedAt(id: Long, lastPlayedAt: Long) {
        localDataSource.updateLastPlayedAt(id, lastPlayedAt)
    }

    override fun getLastPlayedSong(): Flow<Song?> {
        return localDataSource.getLastPlayedSong()
    }
}