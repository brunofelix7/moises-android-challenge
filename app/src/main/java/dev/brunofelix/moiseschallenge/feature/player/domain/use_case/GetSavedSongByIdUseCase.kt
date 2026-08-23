package dev.brunofelix.moiseschallenge.feature.player.domain.use_case

import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for observing a specific song by its ID.
 */
fun interface GetSavedSongByIdUseCase {
    /**
     * Observes a specific song from the local database by its ID.
     * This ensures the Player screen always has data, even without internet.
     *
     * @param id The unique identifier of the song.
     * @return A [Flow] emitting the [Song] if it exists, or null if it hasn't been saved yet.
     */
    operator fun invoke(id: Long): Flow<Song?>
}

class GetSavedSongByIdUseCaseImpl @Inject constructor(
    private val repository: SongRepository
): GetSavedSongByIdUseCase {

    override operator fun invoke(id: Long): Flow<Song?> {
        return repository.observeById(id)
    }
}