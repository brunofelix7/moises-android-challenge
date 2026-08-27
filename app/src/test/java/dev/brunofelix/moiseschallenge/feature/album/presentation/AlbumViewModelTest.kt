package dev.brunofelix.moiseschallenge.feature.album.presentation

import dev.brunofelix.moiseschallenge.R
import dev.brunofelix.moiseschallenge.core.domain.model.Album
import dev.brunofelix.moiseschallenge.core.domain.model.Song
import dev.brunofelix.moiseschallenge.core.domain.util.Resource
import dev.brunofelix.moiseschallenge.core.domain.util.exception.RemoteException
import dev.brunofelix.moiseschallenge.core.presentation.util.UiState
import dev.brunofelix.moiseschallenge.core.presentation.util.UiText
import dev.brunofelix.moiseschallenge.feature.album.domain.use_case.GetAlbumByIdUseCase
import dev.brunofelix.moiseschallenge.feature.song.domain.use_case.SaveRecentSongUseCase
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumViewModelTest : DescribeSpec({

    val testDispatcher = UnconfinedTestDispatcher()
    val getAlbumByIdUseCase = mockk<GetAlbumByIdUseCase>()
    val saveRecentSongUseCase = mockk<SaveRecentSongUseCase>()
    lateinit var viewModel: AlbumViewModel

    val mockSong = Song(
        id = 1L,
        title = "Song Title",
        artist = "Artist Name",
        coverUrl = "url",
        audioUrl = "audio",
        durationMillis = 1000L,
        albumId = 1L
    )

    val mockAlbum = Album(
        id = 1L,
        title = "Album Title",
        artist = "Artist Name",
        coverUrl = "url",
        tracks = listOf(mockSong)
    )

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    beforeTest {
        clearAllMocks()
        viewModel = AlbumViewModel(getAlbumByIdUseCase, saveRecentSongUseCase)
    }

    describe("loadAlbum") {
        it("should update state to Success when use case returns data with tracks") {
            runTest(testDispatcher) {
                coEvery { getAlbumByIdUseCase(1L) } returns Resource.Success(mockAlbum)

                viewModel.loadAlbum(1L)
                
                viewModel.uiState.value shouldBe UiState.Success(mockAlbum)
            }
        }

        it("should update state to Empty when use case returns data without tracks") {
            runTest(testDispatcher) {
                val emptyAlbum = mockAlbum.copy(tracks = emptyList())
                coEvery { getAlbumByIdUseCase(1L) } returns Resource.Success(emptyAlbum)

                viewModel.loadAlbum(1L)
                
                viewModel.uiState.value shouldBe UiState.Empty
            }
        }

        it("should update state to Error when use case returns failure") {
            runTest(testDispatcher) {
                val throwable = RemoteException.Unknown()
                coEvery { getAlbumByIdUseCase(1L) } returns Resource.Error(throwable)

                viewModel.loadAlbum(1L)
                
                viewModel.uiState.value shouldBe UiState.Error(UiText.StringResource(R.string.error_unknown))
            }
        }
    }

    describe("onTrackPlayed") {
        it("should call saveRecentSongUseCase") {
            runTest(testDispatcher) {
                coEvery { saveRecentSongUseCase(mockSong) } returns Unit

                viewModel.onTrackPlayed(mockSong)

                coVerify(exactly = 1) { saveRecentSongUseCase(mockSong) }
            }
        }
    }
})
