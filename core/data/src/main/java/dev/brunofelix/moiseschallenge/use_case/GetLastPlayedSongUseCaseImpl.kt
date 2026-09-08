package dev.brunofelix.moiseschallenge.use_case

import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLastPlayedSongUseCaseImpl @Inject constructor(
    private val repository: SongRepository
) : GetLastPlayedSongUseCase {

    override operator fun invoke(): Flow<Song?> {
        return repository.getLastPlayedSong()
    }
}
