package dev.brunofelix.moiseschallenge.core.domain.use_case

import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

fun interface GetLastPlayedSongUseCase {
    operator fun invoke(): Flow<Song?>
}

class GetLastPlayedSongUseCaseImpl @Inject constructor(
    private val repository: SongRepository
) : GetLastPlayedSongUseCase {

    override operator fun invoke(): Flow<Song?> {
        return repository.getLastPlayedSong()
    }
}