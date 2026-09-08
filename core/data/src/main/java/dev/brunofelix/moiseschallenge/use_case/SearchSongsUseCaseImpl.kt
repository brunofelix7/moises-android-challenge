package dev.brunofelix.moiseschallenge.use_case

import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.repository.SongRepository
import dev.brunofelix.moiseschallenge.util.Resource
import javax.inject.Inject

class SearchSongsUseCaseImpl @Inject constructor(
    private val repository: SongRepository
): SearchSongsUseCase {

    override suspend fun invoke(query: String, limit: Int): Resource<List<Song>> {
        return repository.search(query, limit)
    }
}
