package dev.brunofelix.moiseschallenge.feature.splash.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shouldDisplayLogoImageOnSplashScreen() {
        composeTestRule.setContent {
            AppTheme {
                SplashScreen()
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Opening app")
            .assertIsDisplayed()
    }
}
