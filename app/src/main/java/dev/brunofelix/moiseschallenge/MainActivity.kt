package dev.brunofelix.moiseschallenge

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.brunofelix.moiseschallenge.core.presentation.MoisesApp
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(Color.Transparent.hashCode()),
            statusBarStyle = SystemBarStyle.dark(Color.Transparent.hashCode())
        )
        setContent {
            val view = LocalView.current
            SideEffect {
                val window = (view.context as Activity).window
                val controller = WindowInsetsControllerCompat(window, view)
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false
            }
            AppTheme {
                MoisesApp()
            }
        }
    }
}