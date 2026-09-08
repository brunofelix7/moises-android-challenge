package dev.brunofelix.moiseschallenge.viewmodel

import dev.brunofelix.moiseschallenge.navigation.Route
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class NavigationViewModelTest : DescribeSpec({

    val testDispatcher = UnconfinedTestDispatcher()
    lateinit var viewModel: NavigationViewModel

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    beforeTest {
        viewModel = NavigationViewModel()
    }

    describe("initial state") {
        it("should start with Splash route in backStack") {
            viewModel.backStack.value shouldBe listOf(Route.Splash)
        }
    }

    describe("navigateTo") {
        it("should append route to backStack when it is different from current top route") {
            viewModel.navigateTo(Route.Songs)

            viewModel.backStack.value shouldBe listOf(Route.Splash, Route.Songs)
        }

        it("should not append route to backStack when it is the same as current top route") {
            viewModel.navigateTo(Route.Songs)
            viewModel.navigateTo(Route.Songs)

            viewModel.backStack.value shouldBe listOf(Route.Splash, Route.Songs)
        }
    }

    describe("replaceCurrent") {
        it("should replace the top route in backStack with the new route") {
            viewModel.replaceCurrent(Route.Songs)

            viewModel.backStack.value shouldBe listOf(Route.Songs)
        }

        it("should replace top route when backStack has multiple routes") {
            viewModel.navigateTo(Route.Songs)
            viewModel.replaceCurrent(Route.Album(1L))

            viewModel.backStack.value shouldBe listOf(Route.Splash, Route.Album(1L))
        }
    }

    describe("popBackStack") {
        it("should remove top route when backStack has more than 1 route") {
            viewModel.navigateTo(Route.Songs)
            viewModel.popBackStack()

            viewModel.backStack.value shouldBe listOf(Route.Splash)
        }

        it("should not remove route when backStack has only 1 route") {
            viewModel.popBackStack()

            viewModel.backStack.value shouldBe listOf(Route.Splash)
        }
    }
})