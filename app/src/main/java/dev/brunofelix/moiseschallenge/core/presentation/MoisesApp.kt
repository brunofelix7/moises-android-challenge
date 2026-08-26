package dev.brunofelix.moiseschallenge.core.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventDispatcher
import androidx.navigationevent.NavigationEventDispatcherOwner
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import dev.brunofelix.moiseschallenge.core.presentation.design_system.AppTheme
import dev.brunofelix.moiseschallenge.core.presentation.navigation.NavigationGraph
import dev.brunofelix.moiseschallenge.core.presentation.navigation.NavigationViewModel
import dev.brunofelix.moiseschallenge.core.presentation.navigation.Route

@Composable
fun MoisesApp(
    modifier: Modifier = Modifier,
    viewModel: NavigationViewModel = hiltViewModel()
) {
    val backStack by viewModel.backStack.collectAsStateWithLifecycle()

    MoisesAppContent(
        modifier = modifier,
        backStack = backStack,
        onNavigate = viewModel::navigateTo,
        onReplace = viewModel::replaceCurrent,
        onBack = viewModel::popBackStack
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoisesAppContent(
    backStack: List<Route>,
    onNavigate: (Route) -> Unit,
    onReplace: (Route) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        content = {
            NavigationGraph(
                backStack = backStack,
                onNavigate = onNavigate,
                onReplace = onReplace,
                onBack = onBack
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MoisesAppPreview() {
    val dispatcher = remember { NavigationEventDispatcher() }
    val owner = remember {
        object : NavigationEventDispatcherOwner {
            override val navigationEventDispatcher = dispatcher
        }
    }
    AppTheme {
        CompositionLocalProvider(LocalNavigationEventDispatcherOwner provides owner) {
            MoisesAppContent(
                backStack = listOf(Route.Splash),
                onNavigate = {},
                onReplace = {},
                onBack = {}
            )
        }
    }
}