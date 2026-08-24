package dev.brunofelix.moiseschallenge.core.data.player

import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.player.PlayerRepeatMode
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExoPlayerControllerImplTest {

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var controller: ExoPlayerControllerImpl
    private lateinit var testScope: CoroutineScope

    @Before
    fun setup() {
        runBlocking(Dispatchers.Main) {
            exoPlayer = mockk(relaxed = true)
            testScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
            controller = ExoPlayerControllerImpl(exoPlayer, testScope)
        }
    }

    @After
    fun tearDown() {
        runBlocking(Dispatchers.Main) {
            controller.release()
            testScope.cancel()
        }
    }

    @Test
    fun test_play_should_set_media_item_and_prepare_player() {
        runBlocking(Dispatchers.Main) {
            // Arrange
            val song = Song(audioUrl = "https://example.com/audio.mp3")

            // Act
            controller.play(song)

            // Assert
            verify { exoPlayer.setMediaItem(any()) }
            verify { exoPlayer.prepare() }
            verify { exoPlayer.play() }
        }
    }

    @Test
    fun test_resume_should_call_play_on_exoPlayer() {
        runBlocking(Dispatchers.Main) {
            // Act
            controller.resume()

            // Assert
            verify { exoPlayer.play() }
        }
    }

    @Test
    fun test_pause_should_call_pause_on_exoPlayer() {
        runBlocking(Dispatchers.Main) {
            // Act
            controller.pause()

            // Assert
            verify { exoPlayer.pause() }
        }
    }

    @Test
    fun test_stop_should_reset_position_to_zero_and_pause() {
        runBlocking(Dispatchers.Main) {
            // Act
            controller.stop()

            // Assert
            verify { exoPlayer.pause() }
            verify { exoPlayer.seekTo(0L) }
            controller.currentPosition.value shouldBe 0L
        }
    }

    @Test
    fun test_release_should_call_release_on_exoPlayer() {
        runBlocking(Dispatchers.Main) {
            // Act
            controller.release()

            // Assert
            verify { exoPlayer.release() }
        }
    }

    @Test
    fun test_toggleRepeatMode_should_update_exoPlayer_and_flow() {
        runBlocking(Dispatchers.Main) {
            // Act
            controller.toggleRepeatMode()

            // Assert
            verify { exoPlayer.repeatMode = Player.REPEAT_MODE_ONE }
            controller.repeatMode.value shouldBe PlayerRepeatMode.ONE

            // Act again
            controller.toggleRepeatMode()

            // Assert
            verify { exoPlayer.repeatMode = Player.REPEAT_MODE_OFF }
            controller.repeatMode.value shouldBe PlayerRepeatMode.OFF
        }
    }

    @Test
    fun test_seekTo_should_update_exoPlayer_position_and_flow() {
        runBlocking(Dispatchers.Main) {
            // Arrange
            val position = 5000L

            // Act
            controller.seekTo(position)

            // Assert
            verify { exoPlayer.seekTo(position) }
            controller.currentPosition.value shouldBe position
        }
    }

    @Test
    fun test_moveForward_should_increment_position_by_10_seconds() {
        runBlocking(Dispatchers.Main) {
            // Arrange
            val initialPosition = 5000L
            every { exoPlayer.currentPosition } returns initialPosition
            every { exoPlayer.duration } returns 30000L

            // Act
            controller.moveForward()

            // Assert
            verify { exoPlayer.seekTo(initialPosition + 10000L) }
            controller.currentPosition.value shouldBe (initialPosition + 10000L)
        }
    }

    @Test
    fun test_moveBackward_should_decrement_position_by_10_seconds() {
        runBlocking(Dispatchers.Main) {
            // Arrange
            val initialPosition = 15000L
            every { exoPlayer.currentPosition } returns initialPosition

            // Act
            controller.moveBackward()

            // Assert
            verify { exoPlayer.seekTo(initialPosition - 10000L) }
            controller.currentPosition.value shouldBe (initialPosition - 10000L)
        }
    }
}
