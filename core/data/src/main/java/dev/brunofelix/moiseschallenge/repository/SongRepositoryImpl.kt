package dev.brunofelix.moiseschallenge.repository

import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.util.Resource
import dev.brunofelix.moiseschallenge.util.toResource
import dev.brunofelix.moiseschallenge.local.source.SongLocalDataSource
import dev.brunofelix.moiseschallenge.remote.source.SongRemoteDataSource
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