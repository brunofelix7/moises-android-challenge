package dev.brunofelix.moiseschallenge.core.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dev.brunofelix.moiseschallenge.core.data.local.SongDatabase
import dev.brunofelix.moiseschallenge.core.data.local.entity.SongEntity
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SongDaoTest {

    private lateinit var database: SongDatabase
    private lateinit var songDao: SongDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, SongDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        songDao = database.songDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `should insert a song entity and find it by id`() = runTest {
        // Arrange
        val entity = createSongEntity(id = 1L, title = "Song 1")

        // Act
        val insertedId = songDao.insert(entity)
        val foundEntity = songDao.findById(1L)

        // Assert
        insertedId shouldBe 1L
        foundEntity shouldNotBe null
        foundEntity?.id shouldBe 1L
        foundEntity?.title shouldBe "Song 1"
    }

    @Test
    fun `should return null when song by id does not exist`() = runTest {
        // Act
        val foundEntity = songDao.findById(999L)

        // Assert
        foundEntity shouldBe null
    }

    @Test
    fun `should replace existing song when inserting with same id`() = runTest {
        // Arrange
        val originalEntity = createSongEntity(id = 1L, title = "Original Title", playedAt = 1000L)
        val updatedEntity = createSongEntity(id = 1L, title = "Updated Title", playedAt = 2000L)

        // Act
        songDao.insert(originalEntity)
        songDao.insert(updatedEntity)
        val result = songDao.findById(1L)

        // Assert
        result shouldNotBe null
        result?.title shouldBe "Updated Title"
        result?.playedAt shouldBe 2000L
    }

    @Test
    fun `should return empty list when no songs inserted`() = runTest {
        // Act
        val recentSongs = songDao.getRecentSongs().first()

        // Assert
        recentSongs.shouldBeEmpty()
    }

    @Test
    fun `should emit songs ordered by playedAt descending`() = runTest {
        // Arrange
        val songOldest = createSongEntity(id = 1L, title = "Oldest", playedAt = 1000L)
        val songMiddle = createSongEntity(id = 2L, title = "Middle", playedAt = 2000L)
        val songNewest = createSongEntity(id = 3L, title = "Newest", playedAt = 3000L)

        songDao.insert(songOldest)
        songDao.insert(songMiddle)
        songDao.insert(songNewest)

        // Act
        val recentSongs = songDao.getRecentSongs().first()

        // Assert
        recentSongs shouldHaveSize 3
        recentSongs[0].title shouldBe "Newest"
        recentSongs[1].title shouldBe "Middle"
        recentSongs[2].title shouldBe "Oldest"
    }

    @Test
    fun `should limit recent songs to maximum 50 entries`() = runTest {
        // Arrange
        for (i in 1..60) {
            val entity = createSongEntity(id = i.toLong(), title = "Song $i", playedAt = i.toLong() * 1000)
            songDao.insert(entity)
        }

        // Act
        val recentSongs = songDao.getRecentSongs().first()

        // Assert
        recentSongs shouldHaveSize 50
        recentSongs.first().id shouldBe 60L
        recentSongs.last().id shouldBe 11L
    }

    @Test
    fun `should emit flow with song entity when found`() = runTest {
        // Arrange
        val entity = createSongEntity(id = 10L, title = "Song 10")
        songDao.insert(entity)

        // Act
        val result = songDao.getById(10L).first()

        // Assert
        result shouldNotBe null
        result?.id shouldBe 10L
        result?.title shouldBe "Song 10"
    }

    @Test
    fun `should emit flow with null when song entity is not found`() = runTest {
        // Act
        val result = songDao.getById(999L).first()

        // Assert
        result shouldBe null
    }

    private fun createSongEntity(
        id: Long = 1L,
        title: String = "Test Song",
        artistName: String = "Test Artist",
        coverUrl: String = "https://test.com/cover.jpg",
        audioUrl: String = "https://test.com/audio.mp3",
        durationMillis: Long = 180000L,
        albumId: Long = 100L,
        playedAt: Long = System.currentTimeMillis(),
        lastPlayedAt: Long = 0L
    ): SongEntity {
        return SongEntity(
            id = id,
            title = title,
            artistName = artistName,
            coverUrl = coverUrl,
            audioUrl = audioUrl,
            durationMillis = durationMillis,
            albumId = albumId,
            playedAt = playedAt,
            lastPlayedAt = lastPlayedAt
        )
    }
}
