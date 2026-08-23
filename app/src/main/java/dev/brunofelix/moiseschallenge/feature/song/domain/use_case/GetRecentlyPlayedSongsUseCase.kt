package dev.brunofelix.moiseschallenge.feature.song.domain.use_case

import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for observing recently played songs.
 */
fun interface GetRecentlyPlayedSongsUseCase {
    /**
     * Observes the most recently played songs from the local database.
     * This ensures the Home screen always has data, even without internet.
     *
     * @return A flow of lists of recently played songs.
     */
    operator fun invoke(): Flow<List<Song>>
}

class GetRecentlyPlayedSongsUseCaseImpl @Inject constructor(
    private val repository: SongRepository
) : GetRecentlyPlayedSongsUseCase {

    override operator fun invoke(): Flow<List<Song>> {
        return repository.observeRecentlyPlayed()
    }
}