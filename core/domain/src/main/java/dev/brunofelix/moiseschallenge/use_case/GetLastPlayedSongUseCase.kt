package dev.brunofelix.moiseschallenge.use_case

import dev.brunofelix.moiseschallenge.model.Song
import kotlinx.coroutines.flow.Flow

fun interface GetLastPlayedSongUseCase {
    operator fun invoke(): Flow<Song?>
}
