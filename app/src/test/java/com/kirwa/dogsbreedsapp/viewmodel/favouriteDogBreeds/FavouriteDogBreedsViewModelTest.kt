package com.kirwa.dogsbreedsapp.viewmodel.favouriteDogBreeds

import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import com.kirwa.dogsbreedsapp.domain.usecase.favouriteDogBreeds.FavouriteDogBreedsUseCase
import com.kirwa.dogsbreedsapp.domain.usecase.favouriteDogBreeds.FavouriteDogBreedsUseCaseImpl
import com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.viewmodel.FavouriteDogBreedsViewModel
import com.kirwa.dogsbreedsapp.utils.CoroutineTestRule
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
 * This is the test class to test FavouriteDogBreedsViewModel using Mockk, Junit, Kotest and Kotlinx.
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
        //viewModel = FavouriteDogBreedsViewModel(favouriteDogBreedsUseCase)
    }

    @Test
    fun `initialize then fetch favourite dog breeds successfully`() = runTest {
        // Given
        val breed = FavouriteDogBreed(
            id = 5,
            name = "Akbash Dog",
            weight = "600",
            height = "471",
            bredFor = "Guarding",
            breedGroup = "Working",
            temperament = "Loyal, Independent, Intelligent, Brave",
            origin = "",
            lifeSpan = "lifeSpan",
            referenceImageId = "26pHT3Qk7",
            imageUrl = ""
        )

        every { favouriteDogBreedsUseCase.getLocalFavouriteDogBreeds() } returns flowOf(listOf(breed, breed))

        // When
        viewModel = FavouriteDogBreedsViewModel(favouriteDogBreedsUseCase)

        runCurrent() // Ensure all pending coroutines execute

        // Then
        val state = viewModel.state.first()
        state.favouriteDogBreeds.size shouldBe 2
        state.isRefreshing shouldBe false
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
        state.dogs.size shouldBe 0
        state.isRefreshing shouldBe false

    }
}
