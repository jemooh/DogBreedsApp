package com.kirwa.dogsbreedsapp.viewmodel.favouriteDogBreeds

import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import com.kirwa.dogsbreedsapp.domain.usecase.favouriteDogBreeds.FavouriteDogBreedsUseCase
import com.kirwa.dogsbreedsapp.domain.usecase.favouriteDogBreeds.FavouriteDogBreedsUseCaseImpl
import com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.viewmodel.FavouriteDogBreedsViewModel
import com.kirwa.dogsbreedsapp.utils.CoroutineTestRule
import com.kirwa.dogsbreedsapp.utils.favouriteDogBreedTest
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for [FavouriteDogBreedsViewModel] verifying:
 *
 * 1. Initial loading of favorite dog breeds
 * 2. Empty state handling
 * 3. Proper state transitions
 * 4. Data flow from [FavouriteDogBreedsUseCaseImpl]
 *
 * Tests both successful data loading and empty state scenarios
 * with mocked use case dependencies. Uses coroutine test rules
 * for reliable async operation testing.
 */
@ExperimentalCoroutinesApi
class FavouriteDogBreedsViewModelTest {

    private lateinit var viewModel: FavouriteDogBreedsViewModel

    private val favouriteDogBreedsUseCase = mockk<FavouriteDogBreedsUseCaseImpl>()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setUp() {
        // Initialize the viewModel before each test
        viewModel = FavouriteDogBreedsViewModel(favouriteDogBreedsUseCase)
    }

    @Test
    fun `initialize then fetch favourite dog breeds successfully`() = runTest {
        // Given
        val favouriteDogBreed = favouriteDogBreedTest

        every { favouriteDogBreedsUseCase.getLocalFavouriteDogBreeds() } returns flowOf(listOf(favouriteDogBreed, favouriteDogBreed))

        // When
        viewModel = FavouriteDogBreedsViewModel(favouriteDogBreedsUseCase)

        runCurrent()

        // Then
        val state = viewModel.state.first()
        state.favouriteDogBreeds.size shouldBe 2
        state.isLoading shouldBe false
    }


    @Test
    fun `initialize then fetch favourite dog breeds which has no items`() = runTest {
        // given
        every { favouriteDogBreedsUseCase.getLocalFavouriteDogBreeds() } returns flow {
            emit(emptyList())
        }

        // when
        // Triggering fetch for local favourite dog breeds
        viewModel = FavouriteDogBreedsViewModel(favouriteDogBreedsUseCase)

        // Collect state to ensure test waits for the result
        // then
        // then - Collect only the first emitted value
        val state = viewModel.state.first()
        state.favouriteDogBreeds.size shouldBe 0
        state.isLoading shouldBe false

    }
}
