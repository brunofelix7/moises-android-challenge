package dev.brunofelix.moiseschallenge.feature.player.domain.use_case

import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.repository.SongRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf

class GetSavedSongByIdUseCaseTest : DescribeSpec({

    val repository = mockk<SongRepository>()
    val useCase = GetSavedSongByIdUseCaseImpl(repository)

    beforeTest {
        clearAllMocks()
    }

    describe("invoke") {

        it("should return a Flow emitting the song from repository") {
            // Arrange
            val songId = 1L
            val mockSong = Song(
                id = songId,
                title = "Numb",
                artist = "Linkin Park",
                coverUrl = "url",
                audioUrl = "audio",
                durationMillis = 180000L,
                albumId = 10
            )
            val expectedFlow = flowOf(mockSong)
            every { repository.observeById(songId) } returns expectedFlow

            // Act
            val result = useCase(songId)

            // Assert
            result shouldBe expectedFlow
            verify(exactly = 1) { repository.observeById(songId) }
        }
    }
})
