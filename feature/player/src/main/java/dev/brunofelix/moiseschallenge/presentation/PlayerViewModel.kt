package dev.brunofelix.moiseschallenge.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.brunofelix.moiseschallenge.player.PlayerController
import dev.brunofelix.moiseschallenge.player.PlayerRepeatMode
import dev.brunofelix.moiseschallenge.player.PlayerState
import dev.brunofelix.moiseschallenge.use_case.GetSavedSongByIdUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val getSavedSongByIdUseCase: GetSavedSongByIdUseCase,
    private val playerController: PlayerController
) : ViewModel() {

    private val _uiEvent = Channel<PlayerUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

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
            started = SharingStarted.Lazily,
            initialValue = null
        )

    private val _isSheetVisible = MutableStateFlow(false)

    private val playerUiState: StateFlow<PlayerUiState> = combine(
        song,
        playerState,
        currentPosition,
        duration,
        repeatMode
    ) { song, state, position, duration, repeat ->
        PlayerUiState(
            song = song,
            isPlaying = state == PlayerState.Playing,
            currentPosition = position,
            totalDuration = duration,
            isRepeatEnabled = repeat == PlayerRepeatMode.ONE
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PlayerUiState()
    )

    internal val uiState: StateFlow<PlayerUiState> = combine(
        playerUiState,
        _isSheetVisible
    ) { baseState, isSheetVisible ->
        baseState.copy(isSheetVisible = isSheetVisible)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PlayerUiState()
    )

    fun onAction(action: PlayerUiAction) {
        when (action) {
            PlayerUiAction.OnPlayPause -> {
                if (uiState.value.isPlaying) pause() else resume()
            }
            PlayerUiAction.OnPrevious -> moveBackward()
            PlayerUiAction.OnNext -> moveForward()
            is PlayerUiAction.OnSeek -> seekTo(action.position.toLong())
            PlayerUiAction.OnToggleRepeat -> toggleRepeatMode()
            PlayerUiAction.OnActionClick -> {
                _isSheetVisible.update { true }
            }
            PlayerUiAction.OnDismissSheet -> {
                _isSheetVisible.update { false }
            }
            PlayerUiAction.OnBack -> {
                viewModelScope.launch {
                    _uiEvent.send(PlayerUiEvent.NavigateBack)
                }
            }
            PlayerUiAction.OnViewAlbumClick -> {
                _isSheetVisible.update { false }
                uiState.value.song?.albumId?.let { albumId ->
                    viewModelScope.launch {
                        _uiEvent.send(PlayerUiEvent.NavigateToAlbum(albumId))
                    }
                }
            }
        }
    }

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
