package dev.brunofelix.moiseschallenge.remote.mapper

import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.remote.dto.AlbumDto
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class AlbumDtoMapperTest : DescribeSpec({

    describe("toDomain") {
        it("should correctly map AlbumDto to Album") {
            // Arrange
            val dto = AlbumDto(
                collectionId = 1L,
                collectionName = "Album Name",
                artistName = "Artist Name",
                coverUrl = "cover_url",
                trackCount = 10
            )
            val tracks = listOf(mockk<Song>())

            // Act
            val domain = dto.toDomain(tracks)

            // Assert
            domain.id shouldBe 1L
            domain.title shouldBe "Album Name"
            domain.artist shouldBe "Artist Name"
            domain.coverUrl shouldBe "cover_url"
            domain.tracks shouldBe tracks
        }

        it("should use default values for null fields") {
            // Arrange
            val dto = AlbumDto(
                collectionId = null,
                collectionName = null,
                artistName = null,
                coverUrl = null
            )

            // Act
            val domain = dto.toDomain(emptyList())

            // Assert
            domain.id shouldBe 0L
            domain.title shouldBe "Unknown Album"
            domain.artist shouldBe "Unknown Artist"
            domain.coverUrl shouldBe ""
        }
    }
})
