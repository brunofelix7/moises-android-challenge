package dev.brunofelix.moiseschallenge.remote.mapper

import dev.brunofelix.moiseschallenge.remote.dto.SongDto
import dev.brunofelix.moiseschallenge.remote.mapper.toDomain
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class SongDtoMapperTest : DescribeSpec({

    describe("toDomain") {
        it("should correctly map SongDto to Song") {
            // Arrange
            val dto = SongDto(
                trackId = 1L,
                trackName = "Track Name",
                artistName = "Artist Name",
                coverUrl = "cover_url",
                audioUrl = "audio_url",
                durationMillis = 180000L,
                albumId = 10L
            )

            // Act
            val domain = dto.toDomain()

            // Assert
            domain.id shouldBe 1L
            domain.title shouldBe "Track Name"
            domain.artist shouldBe "Artist Name"
            domain.coverUrl shouldBe "cover_url"
            domain.audioUrl shouldBe "audio_url"
            domain.durationMillis shouldBe 180000L
            domain.albumId shouldBe 10L
        }

        it("should use default values for null fields") {
            // Arrange
            val dto = SongDto(
                trackId = null,
                trackName = null,
                artistName = null,
                coverUrl = null,
                audioUrl = null,
                durationMillis = null,
                albumId = null
            )

            // Act
            val domain = dto.toDomain()

            // Assert
            domain.id shouldBe 0L
            domain.title shouldBe "Unknown Title"
            domain.artist shouldBe "Unknown Artist"
            domain.coverUrl shouldBe ""
            domain.audioUrl shouldBe ""
            domain.durationMillis shouldBe 0L
            domain.albumId shouldBe 0L
        }
    }
})
