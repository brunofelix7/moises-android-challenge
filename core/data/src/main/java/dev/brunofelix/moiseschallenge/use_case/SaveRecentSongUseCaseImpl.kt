package dev.brunofelix.moiseschallenge.use_case

import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.repository.SongRepository
import javax.inject.Inject

class SaveRecentSongUseCaseImpl @Inject constructor(
    private val repository: SongRepository
) : SaveRecentSongUseCase {

    override suspend operator fun invoke(song: Song) {
        repository.saveRecent(song)
    }
}
