package dev.brunofelix.moiseschallenge.feature.song.domain.use_case

import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.repository.SongRepository
import dev.brunofelix.moiseschallenge.core.domain.util.Resource
import dev.brunofelix.moiseschallenge.core.domain.util.exception.RemoteException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class SearchSongsUseCaseTest : DescribeSpec({

    val repository = mockk<SongRepository>()
    val useCase = SearchSongsUseCaseImpl(repository)

    beforeTest {
        clearAllMocks()
    }

    describe("invoke") {

        context("when the repository returns a successful list of songs") {
            it("should return a Resource.Success containing the same list") {
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
                val query = "linkin park"
                coEvery { repository.search(query) } returns Resource.Success(mockSongs)

                // Act
                val result = useCase(query)

                // Assert
                result.shouldBeTypeOf<Resource.Success<List<Song>>>()
                result.data shouldBe mockSongs
                coVerify(exactly = 1) { repository.search(query) }
            }
        }

        context("when the repository returns an empty list") {
            it("should return a Resource.Success containing an empty list") {
                // Arrange
                val query = "unknown artist"
                coEvery { repository.search(query) } returns Resource.Success(emptyList())

                // Act
                val result = useCase(query)

                // Assert
                result.shouldBeTypeOf<Resource.Success<List<Song>>>()
                result.data shouldBe emptyList()
                coVerify(exactly = 1) { repository.search(query) }
            }
        }

        context("when the repository returns an error") {
            it("should return a Resource.Error with the respective exception") {
                // Arrange
                val query = "linkin park"
                val expectedError = RemoteException.NoInternet()
                coEvery { repository.search(query) } returns Resource.Error(expectedError)

                // Act
                val result = useCase(query)

                // Assert
                result.shouldBeTypeOf<Resource.Error>()
                result.throwable shouldBe expectedError
                coVerify(exactly = 1) { repository.search(query) }
            }
        }
    }
})
