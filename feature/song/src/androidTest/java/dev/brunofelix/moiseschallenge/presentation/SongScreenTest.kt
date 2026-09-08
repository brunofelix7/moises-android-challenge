package dev.brunofelix.moiseschallenge.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.theme.AppTheme
import dev.brunofelix.moiseschallenge.util.UiState
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SongScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shouldDisplayEmptyStateWhenRecentSongsIsEmpty() {
        composeTestRule.setContent {
            val emptySearchResults = flowOf(PagingData.empty<Song>()).collectAsLazyPagingItems()
            AppTheme {
                SongScreen(
                    uiState = SongUiState(recentSongsState = UiState.Empty),
                    searchResults = emptySearchResults,
                    onAction = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("No recent songs")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Start exploring! Search for your favorite tracks and they will appear right here")
            .assertIsDisplayed()
    }

    @Test
    fun shouldDisplayRecentSongsListWhenRecentSongsStateIsSuccess() {
        val songs = listOf(
            Song(id = 1, title = "Numb", artist = "Linkin Park")
        )

        composeTestRule.setContent {
            val emptySearchResults = flowOf(PagingData.empty<Song>()).collectAsLazyPagingItems()
            AppTheme {
                SongScreen(
                    uiState = SongUiState(recentSongsState = UiState.Success(songs)),
                    searchResults = emptySearchResults,
                    onAction = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("Numb")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Linkin Park")
            .assertIsDisplayed()
    }

    @Test
    fun shouldTriggerOnShowSearchBarChangeWhenSearchButtonIsClicked() {
        var capturedAction: SongUiAction? = null

        composeTestRule.setContent {
            val emptySearchResults = flowOf(PagingData.empty<Song>()).collectAsLazyPagingItems()
            AppTheme {
                SongScreen(
                    uiState = SongUiState(showSearchBar = false),
                    searchResults = emptySearchResults,
                    onAction = { action -> capturedAction = action }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Search button")
            .performClick()

        capturedAction shouldBe SongUiAction.OnShowSearchBarChange(true)
    }

    @Test
    fun shouldTriggerOnSongClickActionWhenSongItemIsClicked() {
        val song = Song(id = 1, title = "Numb", artist = "Linkin Park")
        var capturedAction: SongUiAction? = null

        composeTestRule.setContent {
            val emptySearchResults = flowOf(PagingData.empty<Song>()).collectAsLazyPagingItems()
            AppTheme {
                SongScreen(
                    uiState = SongUiState(recentSongsState = UiState.Success(listOf(song))),
                    searchResults = emptySearchResults,
                    onAction = { action -> capturedAction = action }
                )
            }
        }

        composeTestRule
            .onNodeWithText("Numb")
            .performClick()

        capturedAction shouldBe SongUiAction.OnSongClick(song)
    }
}
