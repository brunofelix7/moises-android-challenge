package dev.brunofelix.moiseschallenge.use_case

fun interface UpdateLastPlayedSongUseCase {
    suspend operator fun invoke(id: Long)
}
