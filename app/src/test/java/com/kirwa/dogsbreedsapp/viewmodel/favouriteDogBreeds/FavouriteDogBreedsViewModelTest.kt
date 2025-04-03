package com.kirwa.dogsbreedsapp.viewmodel.favouriteDogBreeds

import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import com.kirwa.dogsbreedsapp.domain.usecase.favouriteDogBreeds.FavouriteDogBreedsUseCase
import com.kirwa.dogsbreedsapp.domain.usecase.favouriteDogBreeds.FavouriteDogBreedsUseCaseImpl
import com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.model.FavouriteUiState
import com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.viewmodel.FavouriteDogBreedsViewModel
import com.kirwa.dogsbreedsapp.utils.CoroutineTestRule
import com.kirwa.dogsbreedsapp.utils.favouriteDogBreedTest
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
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
        // Setup default mock behavior before ViewModel initialization
        coEvery { favouriteDogBreedsUseCase.getLocalFavouriteDogBreeds() } returns flowOf(emptyList())
        viewModel = FavouriteDogBreedsViewModel(favouriteDogBreedsUseCase)
    }

    @Test
    fun `initialize then fetch favourite dog breeds successfully`() = runTest {
        // Given
        val favouriteDogBreed = favouriteDogBreedTest
        val testData = listOf(favouriteDogBreed, favouriteDogBreed)

        // Override default mock for this specific test
        coEvery { favouriteDogBreedsUseCase.getLocalFavouriteDogBreeds() } returns flowOf(testData)

        // When - Reinitialize to trigger the new mock behavior
        viewModel = FavouriteDogBreedsViewModel(favouriteDogBreedsUseCase)
        advanceUntilIdle()  // Process coroutines

        // Then
        viewModel.state.value shouldBe FavouriteUiState(
            favouriteDogBreeds = testData,
            isLoading = false
        )
    }

    @Test
    fun `initialize then fetch empty favourite dog breeds`() = runTest {
        // Given - Default mock already returns empty list

        // When
        advanceUntilIdle()  // Process coroutines

        // Then
        viewModel.state.value shouldBe FavouriteUiState(
            favouriteDogBreeds = emptyList(),
            isLoading = false
        )
    }
}
