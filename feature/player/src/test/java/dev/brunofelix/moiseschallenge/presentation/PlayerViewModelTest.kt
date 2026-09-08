package dev.brunofelix.moiseschallenge.presentation

import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.player.PlayerController
import dev.brunofelix.moiseschallenge.player.PlayerRepeatMode
import dev.brunofelix.moiseschallenge.player.PlayerState
import dev.brunofelix.moiseschallenge.use_case.GetSavedSongByIdUseCase
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerViewModelTest : DescribeSpec({

    val testDispatcher = UnconfinedTestDispatcher()
    val getSavedSongByIdUseCase = mockk<GetSavedSongByIdUseCase>()
    val playerController = mockk<PlayerController>(relaxed = true)
    lateinit var viewModel: PlayerViewModel

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
        every { playerController.playerState } returns MutableStateFlow<PlayerState>(PlayerState.Idle).asStateFlow()
        every { playerController.currentPosition } returns MutableStateFlow(0L).asStateFlow()
        every { playerController.duration } returns MutableStateFlow(0L).asStateFlow()
        every { playerController.repeatMode } returns MutableStateFlow(PlayerRepeatMode.OFF).asStateFlow()
        viewModel = PlayerViewModel(getSavedSongByIdUseCase, playerController)
    }

    describe("init") {
        it("should set songId when it is null") {
            val id = 123L
            viewModel.init(id)
            viewModel.songId.value shouldBe id
        }

        it("should not update songId when it is already set") {
            viewModel.init(1L)
            viewModel.init(2L)
            viewModel.songId.value shouldBe 1L
        }
    }

    describe("playback controls") {
        it("should delegate resume to playerController") {
            viewModel.resume()
            verify(exactly = 1) { playerController.resume() }
        }

        it("should delegate pause to playerController") {
            viewModel.pause()
            verify(exactly = 1) { playerController.pause() }
        }

        it("should delegate seekTo to playerController") {
            viewModel.seekTo(100L)
            verify(exactly = 1) { playerController.seekTo(100L) }
        }

        it("should delegate moveForward to playerController") {
            viewModel.moveForward()
            verify(exactly = 1) { playerController.moveForward() }
        }

        it("should delegate moveBackward to playerController") {
            viewModel.moveBackward()
            verify(exactly = 1) { playerController.moveBackward() }
        }

        it("should delegate toggleRepeatMode to playerController") {
            viewModel.toggleRepeatMode()
            verify(exactly = 1) { playerController.toggleRepeatMode() }
        }
    }

    describe("reactive song loading") {
        it("should load song and call play when songId is set") {
            runTest(testDispatcher) {
                every { getSavedSongByIdUseCase(1L) } returns flowOf(mockSong)
                
                val job = backgroundScope.launch { viewModel.song.collect() }
                
                viewModel.init(1L)
                
                verify(exactly = 1) { getSavedSongByIdUseCase(1L) }
                verify(exactly = 1) { playerController.play(mockSong) }
                
                job.cancel()
            }
        }
    }

    describe("uiState combination") {
        it("should combine player controller state and song into PlayerUiState") {
            runTest(testDispatcher) {
                val playerStateFlow = MutableStateFlow<PlayerState>(PlayerState.Playing)
                val positionFlow = MutableStateFlow(5000L)
                val durationFlow = MutableStateFlow(180000L)
                val repeatModeFlow = MutableStateFlow(PlayerRepeatMode.ONE)

                every { playerController.playerState } returns playerStateFlow.asStateFlow()
                every { playerController.currentPosition } returns positionFlow.asStateFlow()
                every { playerController.duration } returns durationFlow.asStateFlow()
                every { playerController.repeatMode } returns repeatModeFlow.asStateFlow()
                every { getSavedSongByIdUseCase(1L) } returns flowOf(mockSong)

                val testVm = PlayerViewModel(getSavedSongByIdUseCase, playerController)

                val job = backgroundScope.launch { testVm.uiState.collect() }

                testVm.init(1L)

                testVm.uiState.value shouldBe PlayerUiState(
                    song = mockSong,
                    isPlaying = true,
                    currentPosition = 5000L,
                    totalDuration = 180000L,
                    isRepeatEnabled = true
                )

                job.cancel()
            }
        }
    }

    describe("onAction") {
        it("should update isSheetVisible state on OnActionClick and OnDismissSheet") {
            runTest(testDispatcher) {
                val job = backgroundScope.launch { viewModel.uiState.collect() }
                viewModel.uiState.value.isSheetVisible shouldBe false

                viewModel.onAction(PlayerUiAction.OnActionClick)
                viewModel.uiState.value.isSheetVisible shouldBe true

                viewModel.onAction(PlayerUiAction.OnDismissSheet)
                viewModel.uiState.value.isSheetVisible shouldBe false

                job.cancel()
            }
        }

        it("should handle OnPlayPause action") {
            runTest(testDispatcher) {
                val job = backgroundScope.launch { viewModel.uiState.collect() }
                viewModel.onAction(PlayerUiAction.OnPlayPause)
                verify(exactly = 1) { playerController.resume() }
                job.cancel()
            }
        }

        it("should emit NavigateBack event on OnBack action") {
            runTest(testDispatcher) {
                val events = mutableListOf<PlayerUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(PlayerUiAction.OnBack)
                events shouldBe listOf(PlayerUiEvent.NavigateBack)

                eventJob.cancel()
            }
        }

        it("should emit NavigateToAlbum event on OnViewAlbumClick action when song has albumId") {
            runTest(testDispatcher) {
                val playerStateFlow = MutableStateFlow<PlayerState>(PlayerState.Playing)
                every { playerController.playerState } returns playerStateFlow.asStateFlow()
                every { getSavedSongByIdUseCase(1L) } returns flowOf(mockSong.copy(albumId = 42L))

                val testVm = PlayerViewModel(getSavedSongByIdUseCase, playerController)
                val stateJob = backgroundScope.launch { testVm.uiState.collect() }
                val events = mutableListOf<PlayerUiEvent>()
                val eventJob = backgroundScope.launch { testVm.uiEvent.collect { events.add(it) } }

                testVm.init(1L)
                testVm.onAction(PlayerUiAction.OnViewAlbumClick)

                events shouldBe listOf(PlayerUiEvent.NavigateToAlbum(42L))

                stateJob.cancel()
                eventJob.cancel()
            }
        }
    }
})
