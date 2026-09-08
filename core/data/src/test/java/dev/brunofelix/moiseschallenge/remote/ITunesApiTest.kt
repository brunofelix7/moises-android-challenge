package dev.brunofelix.moiseschallenge.remote

import dev.brunofelix.moiseschallenge.remote.dto.AlbumDto
import dev.brunofelix.moiseschallenge.remote.dto.SongDto
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class ITunesApiTest : DescribeSpec({

    val mockWebServer = MockWebServer()
    
    val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    val api = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(ITunesApi::class.java)

    afterSpec {
        mockWebServer.shutdown()
    }

    describe("/search") {
        it("should correctly parse search response with songs") {
            // Arrange
            val responseBody = readResource("itunes/search_success_response.json")
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(responseBody)
            )

            // Act
            val response = api.search("One Step Closer")

            // Assert
            response.isSuccessful shouldBe true
            val body = response.body()
            body shouldNotBe null
            body?.resultCount shouldBe 5
            body?.results?.size shouldBe 5
            
            val firstResult = body?.results?.first()
            firstResult.shouldBeTypeOf<SongDto>()
            firstResult.trackName shouldBe "One Step Closer"
            firstResult.artistName shouldBe "LINKIN PARK"
            firstResult.albumName shouldBe "Hybrid Theory (20th Anniversary Edition)"
        }
    }

    describe("/lookup") {
        it("should correctly parse lookup response with album and tracks") {
            // Arrange
            val responseBody = readResource("itunes/lockup_success_response.json")
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(responseBody)
            )

            // Act
            val response = api.lookup(528436018L)

            // Assert
            response.isSuccessful shouldBe true
            val body = response.body()
            body shouldNotBe null
            body?.resultCount shouldBe 13
            
            val results = body?.results ?: emptyList()
            results.size shouldBe 13
            
            val album = results[0]
            album.shouldBeTypeOf<AlbumDto>()
            album.collectionName shouldBe "Hybrid Theory"
            album.artistName shouldBe "LINKIN PARK"
            album.trackCount shouldBe 12
            
            val firstTrack = results[1]
            firstTrack.shouldBeTypeOf<SongDto>()
            firstTrack.trackName shouldBe "Papercut"
            firstTrack.albumName shouldBe "Hybrid Theory"
        }
    }
})

private fun readResource(path: String): String {
    return ITunesApiTest::class.java.classLoader
        ?.getResourceAsStream(path)
        ?.bufferedReader()
        ?.use { it.readText() }
        ?: throw IllegalArgumentException("Resource not found: $path")
}
