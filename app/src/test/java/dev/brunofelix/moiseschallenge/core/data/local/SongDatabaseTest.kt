package dev.brunofelix.moiseschallenge.core.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SongDatabaseTest {

    private lateinit var database: SongDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, SongDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `should provide non-null SongDao instance`() {
        val songDao = database.songDao()
        songDao shouldNotBe null
    }

    @Test
    fun `should open database successfully`() {
        database.openHelper.writableDatabase
        database.isOpen shouldBe true
    }
}
