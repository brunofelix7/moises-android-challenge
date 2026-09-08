package dev.brunofelix.moiseschallenge.use_case

import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.util.Resource

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
     * @return A Resource containing the List<Song> or an error.
     */
    suspend operator fun invoke(query: String, limit: Int = 20): Resource<List<Song>>
}
