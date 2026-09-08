package dev.brunofelix.moiseschallenge.use_case

import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentlyPlayedSongsUseCaseImpl @Inject constructor(
    private val repository: SongRepository
) : GetRecentlyPlayedSongsUseCase {

    override operator fun invoke(): Flow<List<Song>> {
        return repository.observeRecentlyPlayed()
    }
}
