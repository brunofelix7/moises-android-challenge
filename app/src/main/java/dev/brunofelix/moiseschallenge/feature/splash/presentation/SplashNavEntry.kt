package dev.brunofelix.moiseschallenge.feature.splash.presentation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.brunofelix.moiseschallenge.core.presentation.navigation.Route

fun EntryProviderScope<NavKey>.splashNavEntry(
    onNavigateNext: (Route) -> Unit
) {
    entry<Route.Splash> {
        SplashRoute(
            onNavigateNext = onNavigateNext
        )
    }
}