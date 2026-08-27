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
import dev.brunofelix.moiseschallenge.core.presentation.util.UiState
import dev.brunofelix.moiseschallenge.core.presentation.util.extension.toUiText
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.GetRecentlyPlayedSongsUseCase
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.SaveRecentSongUseCase
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.SearchSongsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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

    private val _uiState = MutableStateFlow(SongUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<SongUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val pageSize = 20
    private val maxSearchResults = 100
    private var previousFirstItemId: Long? = null

    /**
     * Retrieves the recently played songs from the database.
     */
    val recentlyPlayedSongs = getRecentlyPlayedSongsUseCase()
        .onEach { delay(500.milliseconds) }
        .onEach { songs ->
            val firstItemId = songs.firstOrNull()?.id
            if (firstItemId != null && previousFirstItemId != null && firstItemId != previousFirstItemId) {
                _uiEvent.emit(SongUiEvent.ScrollToTop)
            }
            previousFirstItemId = firstItemId
        }
        .map { songs ->
            if (songs.isEmpty()) UiState.Empty else UiState.Success(songs)
        }
        .catch { emit(UiState.Error(it.toUiText())) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UiState.Loading
        )

    /**
     * Retrieves the search results based on the current query.
     */
    val searchResults = _uiState
        .map { it.query }
        .distinctUntilChanged()
        .debounce(500.milliseconds)
        .onEach { query ->
            if (query.isNotBlank()) {
                _uiState.update { it.copy(searchState = UiState.Loading) }
            } else {
                _uiState.update { it.copy(searchState = UiState.Initial) }
            }
        }
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(PagingData.empty())
            } else {
                val result = searchSongsUseCase(query, maxSearchResults)
                result.fold(
                    onSuccess = { songs ->
                        _uiState.update {
                            it.copy(searchState = if (songs.isEmpty()) UiState.Empty else UiState.Success(Unit))
                        }
                        Pager(
                            config = PagingConfig(
                                pageSize = pageSize,
                                enablePlaceholders = false
                            )
                        ) {
                            LocalPagingSource(items = songs, pageSize = pageSize)
                        }.flow
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(searchState = UiState.Error(error.toUiText())) }
                        flowOf(PagingData.empty())
                    }
                )
            }
        }
        .cachedIn(viewModelScope)

    /**
     * Updates the query state and triggers a new search.
     * @param query The new query value.
     */
    fun onQueryChange(query: String) {
        _uiState.update {
            it.copy(
                query = query,
                searchState = if (query.isBlank()) UiState.Initial else it.searchState
            )
        }
    }

    /**
     * Retries the current search query.
     */
    fun onRetrySearch() {
        val currentQuery = _uiState.value.query
        onQueryChange("")
        onQueryChange(currentQuery)
    }

    /**
     * Saves the played song to the database if not already present.
     * @param song The song that was played.
     */
    fun onSongPlayed(song: Song) {
        viewModelScope.launch {
            val songs = (recentlyPlayedSongs.value as? UiState.Success)?.data ?: emptyList()
            val alreadySaved = songs.any { it.id == song.id }
            if (!alreadySaved) {
                saveRecentSongUseCase(song)
            }
        }
    }
}
