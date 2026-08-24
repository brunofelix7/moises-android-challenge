package dev.brunofelix.moiseschallenge.core.data.local.mapper

import dev.brunofelix.moiseschallenge.core.data.local.entity.SongEntity
import dev.brunofelix.moiseschallenge.core.domain.model.Song
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class SongEntityMapperTest : DescribeSpec({

    describe("toDomain") {
        it("should correctly map SongEntity to Song") {
            // Arrange
            val entity = SongEntity(
                id = 1L,
                title = "Title",
                artistName = "Artist",
                coverUrl = "cover",
                audioUrl = "audio",
                durationMillis = 1000L,
                albumId = 10L,
                playedAt = 123456789L
            )

            // Act
            val domain = entity.toDomain()

            // Assert
            domain.id shouldBe entity.id
            domain.title shouldBe entity.title
            domain.artist shouldBe entity.artistName
            domain.coverUrl shouldBe entity.coverUrl
            domain.audioUrl shouldBe entity.audioUrl
            domain.durationMillis shouldBe entity.durationMillis
            domain.albumId shouldBe entity.albumId
        }
    }

    describe("toEntity") {
        it("should correctly map Song to SongEntity") {
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

            // Act
            val entity = song.toEntity()

            // Assert
            entity.id shouldBe song.id
            entity.title shouldBe song.title
            entity.artistName shouldBe song.artist
            entity.coverUrl shouldBe song.coverUrl
            entity.audioUrl shouldBe song.audioUrl
            entity.durationMillis shouldBe song.durationMillis
            entity.albumId shouldBe song.albumId
            entity.playedAt shouldNotBe 0L
        }

        it("should use 0L if albumId is null") {
            // Arrange
            val song = Song(
                id = 1L,
                title = "Title",
                artist = "Artist",
                coverUrl = "cover",
                audioUrl = "audio",
                durationMillis = 1000L,
                albumId = null
            )

            // Act
            val entity = song.toEntity()

            // Assert
            entity.albumId shouldBe 0L
        }
    }
})
