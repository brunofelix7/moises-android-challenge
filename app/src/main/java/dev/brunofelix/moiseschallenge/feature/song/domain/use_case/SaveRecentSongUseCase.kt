package dev.brunofelix.moiseschallenge.feature.song.domain.use_case

import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.repository.SongRepository
import javax.inject.Inject

/**
 * Use case for saving a song as recently played.
 *
 * @param song The song to save as recently played.
 * @return Unit
 */
fun interface SaveRecentSongUseCase {
    suspend operator fun invoke(song: Song)
}

class SaveRecentSongUseCaseImpl @Inject constructor(
    private val repository: SongRepository
) : SaveRecentSongUseCase {

    override suspend operator fun invoke(song: Song) {
        repository.saveRecent(song)
    }
}