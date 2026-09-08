package dev.brunofelix.moiseschallenge.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.moiseschallenge.model.Album
import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.theme.AppTheme
import dev.brunofelix.moiseschallenge.util.UiState
import dev.brunofelix.moiseschallenge.util.UiText
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlbumScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shouldRenderAlbumDetailsAndTracksWhenStateIsSuccess() {
        val album = Album(
            id = 1L,
            title = "Meteora",
            artist = "Linkin Park",
            tracks = listOf(
                Song(id = 1L, title = "Numb", artist = "Linkin Park")
            )
        )

        composeTestRule.setContent {
            AppTheme {
                AlbumScreen(
                    uiState = UiState.Success(album),
                    onAction = {}
                )
            }
        }

        composeTestRule
            .onAllNodesWithText("Meteora")
            .onFirst()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Numb")
            .assertIsDisplayed()
    }

    @Test
    fun shouldRenderEmptyStateWhenStateIsEmpty() {
        composeTestRule.setContent {
            AppTheme {
                AlbumScreen(
                    uiState = UiState.Empty,
                    onAction = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("No album found")
            .assertIsDisplayed()
    }

    @Test
    fun shouldRenderErrorStateAndTriggerRetryWhenRetryButtonIsClicked() {
        var capturedAction: AlbumUiAction? = null

        composeTestRule.setContent {
            AppTheme {
                AlbumScreen(
                    uiState = UiState.Error(UiText.DynamicString("Error loading album")),
                    onAction = { action -> capturedAction = action }
                )
            }
        }

        composeTestRule
            .onNodeWithText("Oops!")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Error loading album")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Retry")
            .performClick()

        capturedAction shouldBe AlbumUiAction.OnLoadAlbum
    }

    @Test
    fun shouldTriggerBackActionWhenBackButtonIsClicked() {
        var capturedAction: AlbumUiAction? = null

        composeTestRule.setContent {
            AppTheme {
                AlbumScreen(
                    uiState = UiState.Empty,
                    onAction = { action -> capturedAction = action }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Back button")
            .performClick()

        capturedAction shouldBe AlbumUiAction.OnBack
    }
}
