package dev.brunofelix.moiseschallenge.feature.song.domain.use_case

import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.repository.SongRepository
import dev.brunofelix.moiseschallenge.core.domain.util.Resource
import javax.inject.Inject

/**
 * Use case for searching songs by name.
 */
interface SearchSongsUseCase {
    /**
     * Searches for songs by name.
     * In an offline-first app, this could hit the API
     *
     * @param query The search query.
     * @param limit The maximum number of results to return.
     * @param offset The index of the first result to return.
     * @return A Resource containing the List<Song> or an error.
     */
    suspend operator fun invoke(
        query: String,
        limit: Int = 20,
        offset: Int = 0
    ): Resource<List<Song>>
}

class SearchSongsUseCaseImpl @Inject constructor(
    private val repository: SongRepository
): SearchSongsUseCase {

    override suspend fun invoke(
        query: String,
        limit: Int,
        offset: Int
    ): Resource<List<Song>> {
        return repository.search(query, limit, offset)
    }
}