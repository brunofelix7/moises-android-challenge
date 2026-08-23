package dev.brunofelix.moiseschallenge.feature.song.domain.use_case

import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.repository.SongRepository
import dev.brunofelix.moiseschallenge.core.domain.util.Resource
import javax.inject.Inject

/**
 * Use case for searching songs by name.
 *
 * @param query The name to search for.
 * @return A [Resource] containing a list of songs or an error.
 */
fun interface SearchSongsUseCase {
    suspend operator fun invoke(query: String): Resource<List<Song>>
}

class SearchSongsUseCaseImpl @Inject constructor(
    private val repository: SongRepository
): SearchSongsUseCase {

    override suspend fun invoke(query: String) = repository.search(query)
}