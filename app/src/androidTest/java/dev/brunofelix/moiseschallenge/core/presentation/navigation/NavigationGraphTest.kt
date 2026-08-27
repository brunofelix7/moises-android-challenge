package dev.brunofelix.moiseschallenge.core.presentation.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.moiseschallenge.TestActivity
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationGraphTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    @Test
    fun shouldRenderSplashScreenWhenRouteIsSplash() {
        composeTestRule.setContent {
            AppTheme {
                NavigationGraph(
                    backStack = listOf(Route.Splash),
                    onNavigate = {},
                    onReplace = {},
                    onBack = {}
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Opening app")
            .assertIsDisplayed()
    }

    @Test
    fun shouldRenderSongScreenWhenRouteIsSongs() {
        composeTestRule.setContent {
            AppTheme {
                NavigationGraph(
                    backStack = listOf(Route.Songs),
                    onNavigate = {},
                    onReplace = {},
                    onBack = {}
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Search button")
            .assertIsDisplayed()
    }

    @Test
    fun shouldRenderPlayerScreenWhenRouteIsPlayer() {
        composeTestRule.setContent {
            AppTheme {
                NavigationGraph(
                    backStack = listOf(Route.Player(1L)),
                    onNavigate = {},
                    onReplace = {},
                    onBack = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("Now playing")
            .assertIsDisplayed()
    }

    @Test
    fun shouldRenderAlbumScreenWhenRouteIsAlbum() {
        composeTestRule.setContent {
            AppTheme {
                NavigationGraph(
                    backStack = listOf(Route.Album(100L)),
                    onNavigate = {},
                    onReplace = {},
                    onBack = {}
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Back button")
            .assertIsDisplayed()
    }
}
