package dev.brunofelix.moiseschallenge.use_case

import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedSongByIdUseCaseImpl @Inject constructor(
    private val repository: SongRepository
): GetSavedSongByIdUseCase {

    override operator fun invoke(id: Long): Flow<Song?> {
        return repository.observeById(id)
    }
}
