package dev.brunofelix.moiseschallenge.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.brunofelix.moiseschallenge.model.Album
import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.player.PlayerController
import dev.brunofelix.moiseschallenge.use_case.GetAlbumByIdUseCase
import dev.brunofelix.moiseschallenge.use_case.SaveRecentSongUseCase
import dev.brunofelix.moiseschallenge.use_case.UpdateLastPlayedSongUseCase
import dev.brunofelix.moiseschallenge.util.Resource
import dev.brunofelix.moiseschallenge.util.UiState
import dev.brunofelix.moiseschallenge.util.extension.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val getAlbumByIdUseCase: GetAlbumByIdUseCase,
    private val saveRecentSongUseCase: SaveRecentSongUseCase,
    private val updateLastPlayedSong: UpdateLastPlayedSongUseCase,
    private val playerController: PlayerController
) : ViewModel() {

    private val _uiEvent = Channel<AlbumUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow<UiState<Album>>(UiState.Initial)
    val uiState = _uiState.asStateFlow()

    private var currentAlbumId: Long? = null

    /**
     * Loads the album details for the given album ID.
     * @param albumId The ID of the album to load.
     */
    fun loadAlbum(albumId: Long) {
        if (currentAlbumId == albumId && _uiState.value is UiState.Success) {
            return
        }
        currentAlbumId = albumId
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            when (val result = getAlbumByIdUseCase(albumId)) {
                is Resource.Success -> {
                    _uiState.update {
                        if (result.data.tracks.isEmpty()) {
                            UiState.Empty
                        } else {
                            UiState.Success(result.data)
                        }
                    }
                }
                is Resource.Error -> {
                    _uiState.update { UiState.Error(result.throwable.toUiText()) }
                }
            }
        }
    }

    /**
     * Saves the played song to the database.
     * @param song The song that was played.
     */
    fun onTrackPlayed(song: Song) {
        viewModelScope.launch {
            saveRecentSongUseCase(song)
            updateLastPlayedSong(song.id)
            playerController.play(song)
        }
    }

    fun onAction(action: AlbumUiAction) {
        when (action) {
            AlbumUiAction.OnBack -> {
                viewModelScope.launch {
                    _uiEvent.send(AlbumUiEvent.NavigateBack)
                }
            }
            AlbumUiAction.OnLoadAlbum -> {
                currentAlbumId?.let { loadAlbum(it) }
            }
            is AlbumUiAction.OnTrackClick -> {
                onTrackPlayed(action.song)
            }
        }
    }
}
