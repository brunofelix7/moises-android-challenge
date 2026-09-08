package dev.brunofelix.moiseschallenge.use_case

import dev.brunofelix.moiseschallenge.repository.SongRepository
import javax.inject.Inject

class DeleteRecentSongUseCaseImpl @Inject constructor(
    private val repository: SongRepository
) : DeleteRecentSongUseCase {

    override suspend operator fun invoke(id: Long) {
        repository.deleteRecent(id)
    }
}
