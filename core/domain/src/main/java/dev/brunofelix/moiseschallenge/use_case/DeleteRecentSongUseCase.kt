package dev.brunofelix.moiseschallenge.use_case

/**
 * Use case for deleting a recent song by its ID.
 *
 * @param id The ID of the song to delete.
 * @return Unit
 */
fun interface DeleteRecentSongUseCase {
    suspend operator fun invoke(id: Long)
}
