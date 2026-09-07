package dev.brunofelix.moiseschallenge.feature.song.domain.use_case

import dev.brunofelix.moiseschallenge.core.domain.repository.SongRepository
import javax.inject.Inject

/**
 * Use case for deleting a recent song by its ID.
 *
 * @param id The ID of the song to delete.
 * @return Unit
 */
fun interface DeleteRecentSongUseCase {
    suspend operator fun invoke(id: Long)
}

class DeleteRecentSongUseCaseImpl @Inject constructor(
    private val repository: SongRepository
) : DeleteRecentSongUseCase {

    override suspend operator fun invoke(id: Long) {
        repository.deleteRecent(id)
    }
}
