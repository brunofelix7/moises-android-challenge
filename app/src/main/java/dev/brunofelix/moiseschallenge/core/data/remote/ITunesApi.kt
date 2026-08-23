package dev.brunofelix.moiseschallenge.core.data.remote

import dev.brunofelix.moiseschallenge.core.data.remote.dto.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for communication with the iTunes API.
 */
interface ITunesApi {

    companion object {
        const val BASE_URL = "https://itunes.apple.com/"
    }

    /**
     * Searches for songs based on a term entered by the user.
     *
     * @param term The search term.
     * @param entity The type of results to return. Defaults to "song".
     * @param limit The maximum number of results to return. Defaults to 20.
     * @param offset The index of the first result to return. Defaults to 0.
     * @return A [SearchResponseDto] containing the search results.
     */
    @GET("search")
    suspend fun search(
        @Query("term") term: String,
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): SearchResponseDto

    /**
     * Retrieves the details of an album and its respective tracks using the ID.
     *
     * @param id The ID of the song to lookup.
     * @param entity The type of results to return. Defaults to "song".
     * @return A [SearchResponseDto] containing the lookup results.
     */
    @GET("lookup")
    suspend fun lookup(
        @Query("id") id: Long,
        @Query("entity") entity: String = "song"
    ): SearchResponseDto
}