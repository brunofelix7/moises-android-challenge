package dev.brunofelix.moiseschallenge.core.domain.use_case

import dev.brunofelix.moiseschallenge.core.domain.repository.SongRepository
import javax.inject.Inject

fun interface UpdateLastPlayedSongUseCase {
    suspend operator fun invoke(id: Long)
}

class UpdateLastPlayedSongUseCaseImpl @Inject constructor(
    private val repository: SongRepository
): UpdateLastPlayedSongUseCase {

    override suspend operator fun invoke(id: Long) {
        repository.updateLastPlayedAt(id, System.currentTimeMillis())
    }
}