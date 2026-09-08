package dev.brunofelix.moiseschallenge.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.theme.AppTheme
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayerScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shouldRenderSongTitleAndArtistNameWhenSongIsPresent() {
        val song = Song(id = 1L, title = "In the End", artist = "Linkin Park")

        composeTestRule.setContent {
            AppTheme {
                PlayerScreen(
                    uiState = PlayerUiState(song = song),
                    onAction = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("Now playing")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("In the End")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Linkin Park")
            .assertIsDisplayed()
    }

    @Test
    fun shouldTriggerPlayPauseActionWhenPlayButtonIsClicked() {
        val song = Song(id = 1L, title = "In the End", artist = "Linkin Park")
        var capturedAction: PlayerUiAction? = null

        composeTestRule.setContent {
            AppTheme {
                PlayerScreen(
                    uiState = PlayerUiState(song = song, isPlaying = false),
                    onAction = { action -> capturedAction = action }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Play/pause button")
            .performClick()

        capturedAction shouldBe PlayerUiAction.OnPlayPause
    }

    @Test
    fun shouldTriggerOptionsActionWhenMoreOptionsButtonIsClicked() {
        val song = Song(id = 1L, title = "In the End", artist = "Linkin Park")
        var capturedAction: PlayerUiAction? = null

        composeTestRule.setContent {
            AppTheme {
                PlayerScreen(
                    uiState = PlayerUiState(song = song),
                    onAction = { action -> capturedAction = action }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("More options button")
            .performClick()

        capturedAction shouldBe PlayerUiAction.OnActionClick
    }

    @Test
    fun shouldDisplaySongActionSheetWhenIsSheetVisibleIsTrue() {
        val song = Song(id = 1L, title = "In the End", artist = "Linkin Park")

        composeTestRule.setContent {
            AppTheme {
                PlayerScreen(
                    uiState = PlayerUiState(song = song, isSheetVisible = true),
                    onAction = {}
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("View album button")
            .assertIsDisplayed()
    }
}
