package dev.brunofelix.moiseschallenge.core.data.local.source

import dev.brunofelix.moiseschallenge.core.data.local.dao.SongDao
import dev.brunofelix.moiseschallenge.core.data.local.entity.SongEntity
import dev.brunofelix.moiseschallenge.core.domain.model.Song
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class RoomLocalDataSourceImplTest : DescribeSpec({

    val dao = mockk<SongDao>()
    val dataSource = RoomLocalDataSourceImpl(dao)

    beforeTest {
        clearAllMocks()
    }

    describe("getRecentSongs") {
        it("should return flow of songs mapped from entities") {
            // Arrange
            val entity = SongEntity(1L, "Title", "Artist", "cover", "audio", 1000L, 10L, 123456789L)
            every { dao.getRecentSongs() } returns flowOf(listOf(entity))

            // Act
            val result = dataSource.getRecentSongs().first()

            // Assert
            result.size shouldBe 1
            result[0].id shouldBe 1L
            result[0].title shouldBe "Title"
        }
    }

    describe("observeSongById") {
        it("should return flow of song mapped from entity") {
            // Arrange
            val entity = SongEntity(1L, "Title", "Artist", "cover", "audio", 1000L, 10L, 123456789L)
            every { dao.getById(1L) } returns flowOf(entity)

            // Act
            val result = dataSource.observeSongById(1L).first()

            // Assert
            result?.id shouldBe 1L
            result?.title shouldBe "Title"
        }

        context("when entity is null") {
            it("should emit null") {
                // Arrange
                every { dao.getById(1L) } returns flowOf(null)

                // Act
                val result = dataSource.observeSongById(1L).first()

                // Assert
                result shouldBe null
            }
        }
    }

    describe("saveRecentSong") {
        it("should call dao insert and return id") {
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
            coEvery { dao.insert(any()) } returns 1L

            // Act
            val result = dataSource.saveRecentSong(song)

            // Assert
            result shouldBe 1L
            coVerify(exactly = 1) { dao.insert(any()) }
        }
    }
})
