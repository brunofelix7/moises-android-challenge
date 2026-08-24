package dev.brunofelix.moiseschallenge.feature.song.domain.use_case

import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.repository.SongRepository
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class SaveRecentSongUseCaseTest : DescribeSpec({

    val repository = mockk<SongRepository>()
    val useCase = SaveRecentSongUseCaseImpl(repository)

    beforeTest {
        clearAllMocks()
    }

    describe("invoke") {

        it("should call saveRecent on the repository") {
            // Arrange
            val mockSong = Song(
                id = 1,
                title = "Numb",
                artist = "Linkin Park",
                coverUrl = "url",
                audioUrl = "audio",
                durationMillis = 180000L,
                albumId = 10
            )
            coEvery { repository.saveRecent(mockSong) } returns 1L

            // Act
            useCase(mockSong)

            // Assert
            coVerify(exactly = 1) { repository.saveRecent(mockSong) }
        }
    }
})
