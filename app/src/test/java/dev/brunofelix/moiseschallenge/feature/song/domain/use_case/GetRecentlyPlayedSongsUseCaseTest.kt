package dev.brunofelix.moiseschallenge.feature.song.domain.use_case

import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.repository.SongRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf

class GetRecentlyPlayedSongsUseCaseTest : DescribeSpec({

    val repository = mockk<SongRepository>()
    val useCase = GetRecentlyPlayedSongsUseCaseImpl(repository)

    beforeTest {
        clearAllMocks()
    }

    describe("invoke") {

        it("should return a Flow emitting the list of songs from repository") {
            // Arrange
            val mockSongs = listOf(
                Song(
                    id = 1,
                    title = "Numb",
                    artist = "Linkin Park",
                    coverUrl = "url",
                    audioUrl = "audio",
                    durationMillis = 180000L,
                    albumId = 10
                )
            )
            val expectedFlow = flowOf(mockSongs)
            every { repository.observeRecentlyPlayed() } returns expectedFlow

            // Act
            val result = useCase()

            // Assert
            result shouldBe expectedFlow
            verify(exactly = 1) { repository.observeRecentlyPlayed() }
        }
    }
})
