package dev.brunofelix.moiseschallenge.core.data.repository

import dev.brunofelix.moiseschallenge.core.data.remote.source.SongRemoteDataSource
import dev.brunofelix.moiseschallenge.core.domain.model.Album
import dev.brunofelix.moiseschallenge.core.domain.util.Resource
import dev.brunofelix.moiseschallenge.core.domain.util.exception.RemoteException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class AlbumRepositoryImplTest : DescribeSpec({

    val remoteDataSource = mockk<SongRemoteDataSource>()
    val repository = AlbumRepositoryImpl(remoteDataSource)

    beforeTest {
        clearAllMocks()
    }

    describe("getById") {
        context("when remote call is successful") {
            it("should return success resource") {
                // Arrange
                val album = mockk<Album>()
                coEvery { remoteDataSource.getAlbumDetails(1L) } returns Result.success(album)

                // Act
                val result = repository.getById(1L)

                // Assert
                result.shouldBeTypeOf<Resource.Success<Album>>()
                result.data shouldBe album
            }
        }

        context("when remote call fails") {
            it("should return error resource") {
                // Arrange
                val exception = RemoteException.NotFound()
                coEvery { remoteDataSource.getAlbumDetails(1L) } returns Result.failure(exception)

                // Act
                val result = repository.getById(1L)

                // Assert
                result.shouldBeTypeOf<Resource.Error>()
                result.throwable shouldBe exception
                coVerify(exactly = 1) { remoteDataSource.getAlbumDetails(1L) }
            }
        }
    }
})
