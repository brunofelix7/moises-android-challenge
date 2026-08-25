package dev.brunofelix.moiseschallenge.feature.song.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.util.fold
import dev.brunofelix.moiseschallenge.core.presentation.util.LocalPagingSource
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.GetRecentlyPlayedSongsUseCase
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.SaveRecentSongUseCase
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.SearchSongsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class SongViewModel @Inject constructor(
    getRecentlyPlayedSongsUseCase: GetRecentlyPlayedSongsUseCase,
    private val searchSongsUseCase: SearchSongsUseCase,
    private val saveRecentSongUseCase: SaveRecentSongUseCase
) : ViewModel() {

    val recentlyPlayedSongs = getRecentlyPlayedSongsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _state = MutableStateFlow(SongUiState())
    val state = _state.asStateFlow()

    private val pageSize = 20
    private val maxSearchResults = 60

    val searchResults = _state
        .map { it.query }
        .distinctUntilChanged()
        .debounce(500.milliseconds)
        .onEach { query ->
            if (query.isNotBlank()) {
                _state.update { it.copy(isLoading = true) }
            }
        }
        .flatMapLatest { query ->
            if (query.isBlank()) {
                _state.update { it.copy(isLoading = false) }
                flowOf(PagingData.empty())
            } else {
                val result = searchSongsUseCase(query, maxSearchResults)
                val songs = result.fold(
                    onSuccess = { it },
                    onFailure = { emptyList() }
                )
                _state.update { it.copy(isLoading = false) }
                Pager(
                    config = PagingConfig(
                        pageSize = pageSize,
                        enablePlaceholders = false
                    )
                ) {
                    LocalPagingSource(items = songs, pageSize = pageSize)
                }.flow
            }
        }
        .cachedIn(viewModelScope)

    /**
     * Updates the query state and triggers a new search.
     * @param query The new query value.
     */
    fun onQueryChange(query: String) {
        _state.update {
            it.copy(
                query = query,
                isLoading = if (query.isBlank()) false else it.isLoading
            )
        }
    }

    /**
     * Saves the played song to the database if not already present.
     * @param song The song that was played.
     */
    fun onSongPlayed(song: Song) {
        viewModelScope.launch {
            val alreadySaved = recentlyPlayedSongs.value.any { it.id == song.id }
            if (!alreadySaved) {
                saveRecentSongUseCase(song)
            }
        }
    }
}
