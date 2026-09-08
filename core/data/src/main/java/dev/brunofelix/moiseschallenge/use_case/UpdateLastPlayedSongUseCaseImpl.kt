package dev.brunofelix.moiseschallenge.use_case

import dev.brunofelix.moiseschallenge.repository.SongRepository
import javax.inject.Inject

class UpdateLastPlayedSongUseCaseImpl @Inject constructor(
    private val repository: SongRepository
): UpdateLastPlayedSongUseCase {

    override suspend operator fun invoke(id: Long) {
        repository.updateLastPlayedAt(id, System.currentTimeMillis())
    }
}
