package dev.brunofelix.moiseschallenge.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.brunofelix.moiseschallenge.core.domain.player.PlayerController
import dev.brunofelix.moiseschallenge.core.domain.use_case.GetLastPlayedSongUseCase
import dev.brunofelix.moiseschallenge.core.presentation.util.UiState
import dev.brunofelix.moiseschallenge.core.presentation.util.extension.toUiText
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MiniPlayerViewModel @Inject constructor(
    private val playerController: PlayerController,
    private val getLastPlayedSongUseCase: GetLastPlayedSongUseCase
): ViewModel() {

    val uiState = getLastPlayedSongUseCase()
        .map { song ->
            if (song == null) UiState.Empty else UiState.Success(song)
        }
        .catch { emit(UiState.Error(it.toUiText())) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UiState.Loading
        )

    val playerState = playerController.playerState
    val currentPosition = playerController.currentPosition
    val duration = playerController.duration

    fun resume() {
        val lastSong = (uiState.value as? UiState.Success)?.data
        if (!playerController.hasMediaItem && lastSong != null) {
            playerController.play(lastSong)
        } else {
            playerController.resume()
        }
    }

    fun pause() = playerController.pause()
}