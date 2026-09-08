package dev.brunofelix.moiseschallenge.domain.use_case

import dev.brunofelix.moiseschallenge.model.Album
import dev.brunofelix.moiseschallenge.repository.AlbumRepository
import dev.brunofelix.moiseschallenge.use_case.GetAlbumByIdUseCaseImpl
import dev.brunofelix.moiseschallenge.util.Resource
import dev.brunofelix.moiseschallenge.util.exception.RemoteException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class GetAlbumByIdUseCaseTest : DescribeSpec({

    val repository = mockk<AlbumRepository>()
    val useCase = GetAlbumByIdUseCaseImpl(repository)

    beforeTest {
        clearAllMocks()
    }

    describe("invoke") {

        context("when the repository returns a successful album") {
            it("should return a Resource.Success containing the album") {
                // Arrange
                val albumId = 1L
                val mockAlbum = Album(
                    id = albumId,
                    title = "Hybrid Theory",
                    artist = "Linkin Park",
                    coverUrl = "url",
                    tracks = emptyList()
                )
                coEvery { repository.getById(albumId) } returns Resource.Success(mockAlbum)

                // Act
                val result = useCase(albumId)

                // Assert
                result.shouldBeTypeOf<Resource.Success<Album>>()
                result.data shouldBe mockAlbum
                coVerify(exactly = 1) { repository.getById(albumId) }
            }
        }

        context("when the repository returns an error") {
            it("should return a Resource.Error with the respective exception") {
                // Arrange
                val albumId = 1L
                val expectedError = RemoteException.Unknown()
                coEvery { repository.getById(albumId) } returns Resource.Error(expectedError)

                // Act
                val result = useCase(albumId)

                // Assert
                result.shouldBeTypeOf<Resource.Error>()
                result.throwable shouldBe expectedError
                coVerify(exactly = 1) { repository.getById(albumId) }
            }
        }
    }
})
