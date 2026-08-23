package dev.brunofelix.moiseschallenge.core.data.player

import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.player.PlayerController
import dev.brunofelix.moiseschallenge.core.domain.player.PlayerRepeatMode
import dev.brunofelix.moiseschallenge.core.domain.player.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class ExoPlayerControllerImpl @Inject constructor(
    private val exoPlayer: ExoPlayer
) : PlayerController {

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Idle)
    override val playerState = _playerState.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    override val currentPosition = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    override val duration = _duration.asStateFlow()

    private val _repeatMode = MutableStateFlow(PlayerRepeatMode.OFF)
    override val repeatMode = _repeatMode.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var progressJob: Job? = null

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                if (events.containsAny(
                        Player.EVENT_PLAYBACK_STATE_CHANGED,
                        Player.EVENT_PLAY_WHEN_READY_CHANGED,
                        Player.EVENT_IS_PLAYING_CHANGED
                    )
                ) {
                    updatePlayerState()
                }

                if (events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED) &&
                    player.playbackState == Player.STATE_READY
                ) {
                    _duration.value = player.duration.coerceAtLeast(0L)
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                _playerState.value = PlayerState.Error(error.message ?: "Unknown error")
            }
        })
    }

    override fun play(song: Song) {
        val mediaItem = MediaItem.fromUri(song.audioUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    override fun resume() {
        exoPlayer.play()
    }

    override fun pause() {
        exoPlayer.pause()
    }

    override fun stop() {
        exoPlayer.pause()
        exoPlayer.seekTo(0L)
        _currentPosition.value = 0L
    }

    override fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
        _currentPosition.value = positionMs
    }

    override fun moveForward() {
        val targetPosition = (exoPlayer.currentPosition + 10_000).coerceAtMost(exoPlayer.duration)
        seekTo(targetPosition)
    }

    override fun moveBackward() {
        val targetPosition = (exoPlayer.currentPosition - 10_000).coerceAtLeast(0)
        seekTo(targetPosition)
    }

    override fun toggleRepeatMode() {
        if (_repeatMode.value == PlayerRepeatMode.OFF) {
            exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
            _repeatMode.value = PlayerRepeatMode.ONE
        } else {
            exoPlayer.repeatMode = Player.REPEAT_MODE_OFF
            _repeatMode.value = PlayerRepeatMode.OFF
        }
    }

    private fun updatePlayerState() {
        val state = when (exoPlayer.playbackState) {
            Player.STATE_IDLE -> PlayerState.Idle
            Player.STATE_BUFFERING -> PlayerState.Buffering
            Player.STATE_READY -> if (exoPlayer.isPlaying) PlayerState.Playing else PlayerState.Paused
            Player.STATE_ENDED -> PlayerState.Ended
            else -> PlayerState.Idle
        }
        _playerState.value = state

        if (exoPlayer.isPlaying) startProgressTracker() else stopProgressTracker()
    }

    private fun startProgressTracker() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (isActive) {
                _currentPosition.value = exoPlayer.currentPosition
                delay(500.milliseconds)
            }
        }
    }

    private fun stopProgressTracker() {
        progressJob?.cancel()
    }
}