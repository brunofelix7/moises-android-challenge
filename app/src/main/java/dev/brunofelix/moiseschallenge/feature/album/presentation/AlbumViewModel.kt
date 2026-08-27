package dev.brunofelix.moiseschallenge.feature.album.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.brunofelix.moiseschallenge.core.domain.model.Album
import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.util.Resource
import dev.brunofelix.moiseschallenge.core.presentation.util.UiState
import dev.brunofelix.moiseschallenge.core.presentation.util.extension.toUiText
import dev.brunofelix.moiseschallenge.feature.album.domain.use_case.GetAlbumByIdUseCase
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.SaveRecentSongUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val getAlbumByIdUseCase: GetAlbumByIdUseCase,
    private val saveRecentSongUseCase: SaveRecentSongUseCase
) : ViewModel() {

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
        }
    }
}
