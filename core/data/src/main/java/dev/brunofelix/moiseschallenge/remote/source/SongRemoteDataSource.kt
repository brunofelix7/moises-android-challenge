package dev.brunofelix.moiseschallenge.remote.source

import dev.brunofelix.moiseschallenge.model.Album
import dev.brunofelix.moiseschallenge.model.Song

/**
 * Interface for remote data source operations related to songs.
 */
interface SongRemoteDataSource {
    /**
     * Searches for songs based on a term entered by the user.
     *
     * @param term The search term.
     * @param limit The maximum number of results to return.
     * @return A [Result] containing the search results.
     */
    suspend fun search(term: String, limit: Int): Result<List<Song>>

    /**
     * Retrieves the details of an album and its respective tracks using the ID.
     *
     * @param id The ID of the song to lookup.
     * @return A [Result] containing the lookup results.
     */
    suspend fun getAlbumDetails(id: Long): Result<Album>
}