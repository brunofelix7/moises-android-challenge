package dev.brunofelix.moiseschallenge.feature.song.presentation

import dev.brunofelix.moiseschallenge.R
import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.player.PlayerController
import dev.brunofelix.moiseschallenge.core.domain.use_case.UpdateLastPlayedSongUseCase
import dev.brunofelix.moiseschallenge.core.domain.util.Resource
import dev.brunofelix.moiseschallenge.core.presentation.util.UiState
import dev.brunofelix.moiseschallenge.core.presentation.util.UiText
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.DeleteRecentSongUseCase
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.GetRecentlyPlayedSongsUseCase
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.SaveRecentSongUseCase
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.SearchSongsUseCase
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class SongViewModelTest : DescribeSpec({

    val testDispatcher = UnconfinedTestDispatcher()
    val getRecentlyPlayedSongsUseCase = mockk<GetRecentlyPlayedSongsUseCase>()
    val searchSongsUseCase = mockk<SearchSongsUseCase>()
    val saveRecentSongUseCase = mockk<SaveRecentSongUseCase>()
    val updateLastPlayedSong = mockk<UpdateLastPlayedSongUseCase>()
    val deleteRecentSongUseCase = mockk<DeleteRecentSongUseCase>()
    val playerController = mockk<PlayerController>(relaxed = true)
    lateinit var viewModel: SongViewModel

    val mockSong = Song(
        id = 1L,
        title = "Song Title",
        artist = "Artist Name",
        coverUrl = "url",
        audioUrl = "audio",
        durationMillis = 1000L,
        albumId = 1L
    )

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    beforeTest {
        clearAllMocks()
        every { getRecentlyPlayedSongsUseCase() } returns flowOf(emptyList())
        coEvery { updateLastPlayedSong(any()) } returns Unit
        viewModel = SongViewModel(getRecentlyPlayedSongsUseCase, searchSongsUseCase, saveRecentSongUseCase, updateLastPlayedSong, deleteRecentSongUseCase, playerController)
    }

    describe("recentlyPlayedSongs") {
        it("should emit Loading and then Success when use case emits songs") {
            runTest(testDispatcher) {
                val mockSongs = listOf(mockSong)
                every { getRecentlyPlayedSongsUseCase() } returns flowOf(mockSongs)
                val vm = SongViewModel(getRecentlyPlayedSongsUseCase, searchSongsUseCase, saveRecentSongUseCase, updateLastPlayedSong, deleteRecentSongUseCase, playerController)
                val job = backgroundScope.launch { vm.recentlyPlayedSongs.collect() }
                
                vm.recentlyPlayedSongs.value shouldBe UiState.Loading
                
                advanceTimeBy(501.milliseconds)
                advanceUntilIdle()
                
                vm.recentlyPlayedSongs.value shouldBe UiState.Success(mockSongs)
                job.cancel()
            }
        }

        it("should emit Empty when use case emits empty list") {
            runTest(testDispatcher) {
                every { getRecentlyPlayedSongsUseCase() } returns flowOf(emptyList())
                val vm = SongViewModel(getRecentlyPlayedSongsUseCase, searchSongsUseCase, saveRecentSongUseCase, updateLastPlayedSong, deleteRecentSongUseCase, playerController)
                val job = backgroundScope.launch { vm.recentlyPlayedSongs.collect() }
                
                advanceTimeBy(501.milliseconds)
                advanceUntilIdle()
                
                vm.recentlyPlayedSongs.value shouldBe UiState.Empty
                job.cancel()
            }
        }

        it("should emit ScrollToTop event when first item changes") {
            runTest(testDispatcher) {
                val song1 = mockSong.copy(id = 1L)
                val song2 = mockSong.copy(id = 2L)
                val flow = MutableSharedFlow<List<Song>>(replay = 1)
                every { getRecentlyPlayedSongsUseCase() } returns flow
                val vm = SongViewModel(getRecentlyPlayedSongsUseCase, searchSongsUseCase, saveRecentSongUseCase, updateLastPlayedSong, deleteRecentSongUseCase, playerController)
                val events = mutableListOf<SongUiEvent>()
                val eventJob = backgroundScope.launch { vm.uiEvent.collect { events.add(it) } }
                val stateJob = backgroundScope.launch { vm.recentlyPlayedSongs.collect() }

                flow.emit(listOf(song1))
                advanceTimeBy(501.milliseconds)
                advanceUntilIdle()

                events.isEmpty() shouldBe true

                flow.emit(listOf(song2, song1))
                advanceTimeBy(501.milliseconds)
                advanceUntilIdle()

                events shouldBe listOf(SongUiEvent.ScrollToTop)

                eventJob.cancel()
                stateJob.cancel()
            }
        }
    }

    describe("onQueryChange") {
        it("should update state with new query") {
            runTest(testDispatcher) {
                val job = backgroundScope.launch { viewModel.uiState.collect() }
                val query = "test"
                viewModel.onQueryChange(query)
                viewModel.uiState.value.query shouldBe query
                job.cancel()
            }
        }

        it("should reset searchState to Initial when query becomes blank") {
            runTest(testDispatcher) {
                val job = backgroundScope.launch { viewModel.uiState.collect() }
                viewModel.onQueryChange("test")
                viewModel.onQueryChange("")
                viewModel.uiState.value.searchState shouldBe UiState.Initial
                job.cancel()
            }
        }
    }

    describe("searchResults") {
        it("should update searchState to Loading then Success when query is entered") {
            runTest(testDispatcher) {
                val query = "linkin park"
                val mockSongs = listOf(mockSong)
                coEvery { searchSongsUseCase(any(), any()) } coAnswers {
                    delay(100.milliseconds)
                    Resource.Success(mockSongs)
                }
                val jobState = backgroundScope.launch { viewModel.uiState.collect() }
                val jobResults = backgroundScope.launch { viewModel.searchResults.collect() }
                
                viewModel.onQueryChange(query)
                
                advanceTimeBy(501.milliseconds)
                viewModel.uiState.value.searchState shouldBe UiState.Loading
                advanceUntilIdle()
                
                viewModel.uiState.value.searchState shouldBe UiState.Success(Unit)
                jobResults.cancel()
                jobState.cancel()
            }
        }

        it("should update searchState to Error when search fails") {
            runTest(testDispatcher) {
                val query = "error"
                coEvery { searchSongsUseCase(any(), any()) } returns Resource.Error(Exception("error"))
                val jobState = backgroundScope.launch { viewModel.uiState.collect() }
                val jobResults = backgroundScope.launch { viewModel.searchResults.collect() }
                
                viewModel.onQueryChange(query)
                advanceTimeBy(501.milliseconds)
                advanceUntilIdle()
                
                viewModel.uiState.value.searchState.shouldBeTypeOf<UiState.Error>()
                (viewModel.uiState.value.searchState as UiState.Error).uiText shouldBe UiText.StringResource(R.string.error_unknown)
                jobResults.cancel()
                jobState.cancel()
            }
        }
    }

    describe("onSongPlayed") {
        it("should call saveRecentSongUseCase and updateLastPlayedSong when song is played") {
            runTest(testDispatcher) {
                every { getRecentlyPlayedSongsUseCase() } returns flowOf(emptyList())
                coEvery { saveRecentSongUseCase(mockSong) } returns Unit
                val vm = SongViewModel(getRecentlyPlayedSongsUseCase, searchSongsUseCase, saveRecentSongUseCase, updateLastPlayedSong, deleteRecentSongUseCase, playerController)
                val job = backgroundScope.launch { vm.recentlyPlayedSongs.collect() }
                
                advanceTimeBy(501.milliseconds)
                advanceUntilIdle()

                vm.onSongPlayed(mockSong)
                advanceUntilIdle()

                coVerify(exactly = 1) { saveRecentSongUseCase(mockSong) }
                coVerify(exactly = 1) { updateLastPlayedSong(mockSong.id) }
                coVerify(exactly = 1) { playerController.play(mockSong) }
                job.cancel()
            }
        }
    }

    describe("onDeleteRecentSong") {
        it("should call deleteRecentSongUseCase and stop player if song is currently playing") {
            runTest(testDispatcher) {
                every { playerController.currentAudioUrl } returns mockSong.audioUrl
                coEvery { deleteRecentSongUseCase(mockSong.id) } returns Unit
                viewModel.onDeleteRecentSong(mockSong)
                advanceUntilIdle()

                coVerify(exactly = 1) { deleteRecentSongUseCase(mockSong.id) }
                verify(exactly = 1) { playerController.stop() }
            }
        }

        it("should call deleteRecentSongUseCase without stopping player if song is not playing") {
            runTest(testDispatcher) {
                every { playerController.currentAudioUrl } returns "other_url"
                coEvery { deleteRecentSongUseCase(mockSong.id) } returns Unit
                viewModel.onDeleteRecentSong(mockSong)
                advanceUntilIdle()

                coVerify(exactly = 1) { deleteRecentSongUseCase(mockSong.id) }
                verify(exactly = 0) { playerController.stop() }
            }
        }
    }
})
