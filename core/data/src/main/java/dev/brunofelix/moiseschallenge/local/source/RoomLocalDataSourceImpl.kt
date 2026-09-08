package dev.brunofelix.moiseschallenge.local.source

import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.local.dao.SongDao
import dev.brunofelix.moiseschallenge.local.mapper.toDomain
import dev.brunofelix.moiseschallenge.local.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomLocalDataSourceImpl @Inject constructor(
    private val dao: SongDao
) : SongLocalDataSource {

    override fun getRecentSongs(): Flow<List<Song>> {
        return dao.getRecentSongs().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeSongById(id: Long): Flow<Song?> {
        return dao.getById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun saveRecentSong(song: Song): Long {
        val existing = dao.findById(song.id)
        if (existing != null) {
            return song.id
        }
        return dao.insert(song.toEntity())
    }

    override suspend fun deleteRecentSong(id: Long) {
        dao.delete(id)
    }

    override suspend fun updateLastPlayedAt(id: Long, lastPlayedAt: Long) {
        dao.updateLastPlayedAt(id, lastPlayedAt)
    }

    override fun getLastPlayedSong(): Flow<Song?> {
        return dao.getLastPlayedSong().map { entity ->
            entity?.toDomain()
        }
    }
}