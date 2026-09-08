package dev.brunofelix.moiseschallenge.local.source

import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.local.dao.SongDao
import dev.brunofelix.moiseschallenge.local.entity.SongEntity
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

class RoomLocalDataSourceImplTest : DescribeSpec({

    val dao = mockk<SongDao>()
    val dataSource = RoomLocalDataSourceImpl(dao)

    beforeTest {
        clearAllMocks()
    }

    describe("getRecentSongs") {
        it("should return flow of songs mapped from entities") {
            runTest {
                // Arrange
                val entity = SongEntity(
                    1L,
                    "Title",
                    "Artist",
                    "cover",
                    "audio",
                    1000L,
                    10L,
                    123456789L,
                    123456789L
                )
                every { dao.getRecentSongs() } returns flowOf(listOf(entity))

                // Act
                val result = dataSource.getRecentSongs().first()

                // Assert
                result.size shouldBe 1
                result[0].id shouldBe 1L
                result[0].title shouldBe "Title"
            }
        }
    }

    describe("observeSongById") {
        it("should return flow of song mapped from entity") {
            runTest {
                // Arrange
                val entity = SongEntity(1L, "Title", "Artist", "cover", "audio", 1000L, 10L, 123456789L, 123456789L)
                every { dao.getById(1L) } returns flowOf(entity)

                // Act
                val result = dataSource.observeSongById(1L).first()

                // Assert
                result?.id shouldBe 1L
                result?.title shouldBe "Title"
            }
        }

        context("when entity is null") {
            it("should emit null") {
                runTest {
                    // Arrange
                    every { dao.getById(1L) } returns flowOf(null)

                    // Act
                    val result = dataSource.observeSongById(1L).first()

                    // Assert
                    result shouldBe null
                }
            }
        }
    }

    describe("saveRecentSong") {
        it("should call dao insert and return id when song does not exist") {
            runTest {
                // Arrange
                val song = Song(
                    id = 1L,
                    title = "Title",
                    artist = "Artist",
                    coverUrl = "cover",
                    audioUrl = "audio",
                    durationMillis = 1000L,
                    albumId = 10L
                )
                coEvery { dao.findById(1L) } returns null
                coEvery { dao.insert(any()) } returns 1L

                // Act
                val result = dataSource.saveRecentSong(song)

                // Assert
                result shouldBe 1L
                coVerify(exactly = 1) { dao.insert(any()) }
            }
        }

        it("should not call dao insert when song already exists in recent list") {
            runTest {
                // Arrange
                val song = Song(
                    id = 1L,
                    title = "Title",
                    artist = "Artist",
                    coverUrl = "cover",
                    audioUrl = "audio",
                    durationMillis = 1000L,
                    albumId = 10L
                )
                val entity = SongEntity(1L, "Title", "Artist", "cover", "audio", 1000L, 10L, 123456789L, 123456789L)
                coEvery { dao.findById(1L) } returns entity

                // Act
                val result = dataSource.saveRecentSong(song)

                // Assert
                result shouldBe 1L
                coVerify(exactly = 0) { dao.insert(any()) }
            }
        }
    }
})
