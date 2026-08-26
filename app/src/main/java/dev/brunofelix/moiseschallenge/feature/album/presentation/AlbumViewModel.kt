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

    private val _state = MutableStateFlow<UiState<Album>>(UiState.Initial)
    val state = _state.asStateFlow()

    fun loadAlbum(albumId: Long) {
        viewModelScope.launch {
            _state.update { UiState.Loading }
            when (val result = getAlbumByIdUseCase(albumId)) {
                is Resource.Success -> {
                    _state.update {
                        if (result.data.tracks.isEmpty()) {
                            UiState.Empty
                        } else {
                            UiState.Success(result.data)
                        }
                    }
                }
                is Resource.Error -> {
                    _state.update { UiState.Error(result.throwable.toUiText()) }
                }
            }
        }
    }

    fun onTrackPlayed(song: Song) {
        viewModelScope.launch {
            saveRecentSongUseCase(song)
        }
    }
}
