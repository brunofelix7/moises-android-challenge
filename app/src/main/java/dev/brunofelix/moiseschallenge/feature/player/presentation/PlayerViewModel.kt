package dev.brunofelix.moiseschallenge.feature.player.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.brunofelix.moiseschallenge.core.domain.player.PlayerController
import dev.brunofelix.moiseschallenge.feature.player.domain.use_case.GetSavedSongByIdUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val getSavedSongByIdUseCase: GetSavedSongByIdUseCase,
    private val playerController: PlayerController
) : ViewModel() {

    private val _songId = MutableStateFlow<Long?>(null)
    val songId = _songId.asStateFlow()

    val playerState = playerController.playerState
    val currentPosition = playerController.currentPosition
    val duration = playerController.duration
    val repeatMode = playerController.repeatMode

    val song = _songId
        .filterNotNull()
        .flatMapLatest { id ->
            getSavedSongByIdUseCase(id)
        }
        .onEach { loadedSong ->
            if (loadedSong != null) {
                playerController.play(loadedSong)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun init(id: Long) {
        if (_songId.value == null) {
            _songId.value = id
        }
    }

    fun resume() = playerController.resume()

    fun pause() = playerController.pause()

    fun seekTo(positionMs: Long) = playerController.seekTo(positionMs)

    fun moveForward() = playerController.moveForward()

    fun moveBackward() = playerController.moveBackward()

    fun toggleRepeatMode() = playerController.toggleRepeatMode()
}
