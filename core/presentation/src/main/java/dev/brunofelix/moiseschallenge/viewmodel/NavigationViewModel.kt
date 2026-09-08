package dev.brunofelix.moiseschallenge.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.brunofelix.moiseschallenge.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * ViewModel for handling navigation in the application.
 */
@HiltViewModel
class NavigationViewModel @Inject constructor() : ViewModel() {

    private val startDestination = Route.Splash

    private val _backStack = MutableStateFlow<List<Route>>(listOf(startDestination))
    val backStack = _backStack.asStateFlow()

    /**
     * Navigate to a specific route and replace the current route in the back stack.
     */
    fun replaceCurrent(route: Route) {
        _backStack.update { currentStack ->
            currentStack.dropLast(1) + route
        }
    }

    /**
     * Navigate to a specific route.
     * @param route The route to navigate to.
     */
    fun navigateTo(route: Route) {
        _backStack.update { currentStack ->
            if (currentStack.lastOrNull() == route) {
                currentStack
            } else {
                currentStack + route
            }
        }
    }

    /**
     * Pop the current route from the back stack.
     */
    fun popBackStack() {
        _backStack.update { currentStack ->
            if (currentStack.size > 1) {
                currentStack.dropLast(1)
            } else {
                currentStack
            }
        }
    }
}