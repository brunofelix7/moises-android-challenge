package dev.brunofelix.moiseschallenge.core.data.remote.source

import dev.brunofelix.moiseschallenge.core.data.remote.ITunesApi
import dev.brunofelix.moiseschallenge.core.data.remote.dto.AlbumDto
import dev.brunofelix.moiseschallenge.core.data.remote.dto.SearchResponseDto
import dev.brunofelix.moiseschallenge.core.data.remote.dto.SongDto
import dev.brunofelix.moiseschallenge.core.domain.util.exception.RemoteException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class ITunesRemoteDataSourceImplTest : DescribeSpec({

    val api = mockk<ITunesApi>()
    val dataSource = ITunesRemoteDataSourceImpl(api)

    beforeTest {
        clearAllMocks()
    }

    describe("search") {
        context("when api call is successful") {
            it("should return success result with songs") {
                // Arrange
                val songDto = SongDto(trackId = 1L, trackName = "Song", artistName = "Artist")
                val responseDto = SearchResponseDto(resultCount = 1, results = listOf(songDto))
                coEvery { api.search("term", "song", 20) } returns Response.success(responseDto)

                // Act
                val result = dataSource.search("term", 20, 0)

                // Assert
                result.isSuccess shouldBe true
                val songs = result.getOrNull()
                songs?.size shouldBe 1
                songs?.get(0)?.id shouldBe 1L
            }
        }

        context("when api call fails") {
            it("should return failure result with exception") {
                // Arrange
                coEvery { api.search(any(), any(), any()) } returns Response.error(404, "".toResponseBody())

                // Act
                val result = dataSource.search("term", 20, 0)

                // Assert
                result.isFailure shouldBe true
                result.exceptionOrNull().shouldBeInstanceOf<RemoteException.NotFound>()
            }
        }
    }

    describe("getAlbumDetails") {
        context("when api call returns album and songs") {
            it("should return success result with album including tracks") {
                // Arrange
                val albumDto = AlbumDto(collectionId = 10L, collectionName = "Album", artistName = "Artist")
                val songDto = SongDto(trackId = 1L, trackName = "Song", albumId = 10L)
                val responseDto = SearchResponseDto(resultCount = 2, results = listOf(albumDto, songDto))
                coEvery { api.lookup(10L) } returns Response.success(responseDto)

                // Act
                val result = dataSource.getAlbumDetails(10L)

                // Assert
                result.isSuccess shouldBe true
                val album = result.getOrNull()
                album?.id shouldBe 10L
                album?.tracks?.size shouldBe 1
                album?.tracks?.get(0)?.id shouldBe 1L
            }
        }

        context("when album is not found in results") {
            it("should return failure result with NotFound exception") {
                // Arrange
                val responseDto = SearchResponseDto(resultCount = 0, results = emptyList())
                coEvery { api.lookup(any()) } returns Response.success(responseDto)

                // Act
                val result = dataSource.getAlbumDetails(10L)

                // Assert
                result.isFailure shouldBe true
                result.exceptionOrNull().shouldBeInstanceOf<RemoteException.NotFound>()
            }
        }
    }
})
