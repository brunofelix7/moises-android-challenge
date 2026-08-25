package dev.brunofelix.moiseschallenge.core.data.repository

import dev.brunofelix.moiseschallenge.core.data.local.source.SongLocalDataSource
import dev.brunofelix.moiseschallenge.core.data.remote.source.SongRemoteDataSource
import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.util.Resource
import dev.brunofelix.moiseschallenge.core.domain.util.exception.RemoteException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf

class SongRepositoryImplTest : DescribeSpec({

    val remoteDataSource = mockk<SongRemoteDataSource>()
    val localDataSource = mockk<SongLocalDataSource>()
    val repository = SongRepositoryImpl(remoteDataSource, localDataSource)

    beforeTest {
        clearAllMocks()
    }

    describe("search") {
        context("when remote search is successful") {
            it("should return success resource") {
                // Arrange
                val songs = listOf(mockk<Song>())
                coEvery { remoteDataSource.search("term", 20) } returns Result.success(songs)

                // Act
                val result = repository.search("term", 20)

                // Assert
                result.shouldBeTypeOf<Resource.Success<List<Song>>>()
                result.data shouldBe songs
            }
        }

        context("when remote search fails") {
            it("should return error resource") {
                // Arrange
                val exception = RemoteException.NoInternet()
                coEvery { remoteDataSource.search("term", 20) } returns Result.failure(exception)

                // Act
                val result = repository.search("term", 20)

                // Assert
                result.shouldBeTypeOf<Resource.Error>()
                result.throwable shouldBe exception
            }
        }
    }

    describe("saveRecent") {
        it("should call local data source saveRecentSong") {
            // Arrange
            val song = mockk<Song>()
            coEvery { localDataSource.saveRecentSong(song) } returns 1L

            // Act
            val result = repository.saveRecent(song)

            // Assert
            result shouldBe 1L
            coVerify(exactly = 1) { localDataSource.saveRecentSong(song) }
        }
    }

    describe("observeById") {
        it("should return flow from local data source") {
            // Arrange
            val song = mockk<Song>()
            val flow = flowOf(song)
            every { localDataSource.observeSongById(1L) } returns flow

            // Act
            val result = repository.observeById(1L)

            // Assert
            result shouldBe flow
            verify(exactly = 1) { localDataSource.observeSongById(1L) }
        }
    }

    describe("observeRecentlyPlayed") {
        it("should return flow from local data source") {
            // Arrange
            val songs = listOf(mockk<Song>())
            val flow = flowOf(songs)
            every { localDataSource.getRecentSongs() } returns flow

            // Act
            val result = repository.observeRecentlyPlayed()

            // Assert
            result shouldBe flow
            verify(exactly = 1) { localDataSource.getRecentSongs() }
        }
    }
})
