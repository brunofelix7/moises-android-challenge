package dev.brunofelix.moiseschallenge.core.data.local.source

import dev.brunofelix.moiseschallenge.core.data.local.dao.SongDao
import dev.brunofelix.moiseschallenge.core.data.local.mapper.toDomain
import dev.brunofelix.moiseschallenge.core.data.local.mapper.toEntity
import dev.brunofelix.moiseschallenge.core.domain.model.Song
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
        return dao.insert(song.toEntity())
    }
}