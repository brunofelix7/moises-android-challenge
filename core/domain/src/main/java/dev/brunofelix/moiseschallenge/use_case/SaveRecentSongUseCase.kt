package dev.brunofelix.moiseschallenge.use_case

import dev.brunofelix.moiseschallenge.model.Song

/**
 * Use case for saving a song as recently played.
 *
 * @param song The song to save as recently played.
 * @return Unit
 */
fun interface SaveRecentSongUseCase {
    suspend operator fun invoke(song: Song)
}
