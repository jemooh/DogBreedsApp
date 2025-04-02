package com.kirwa.dogsbreedsapp.viewmodel.dogBreedList

import com.kirwa.dogsbreedsapp.domain.usecase.dogBreedsList.DogBreedsListUseCase
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.viewmodel.DogBreedsListViewModel
import com.kirwa.dogsbreedsapp.utils.CoroutineTestRule
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import com.kirwa.dogsbreedsapp.data.remote.model.Result
import kotlinx.coroutines.test.setMain
import io.mockk.coEvery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * This is the test class to test DogBreedsListViewModel using Mockk, Junit, Kotest and Kotlinx.
 */



@ExperimentalCoroutinesApi
class DogBreedsListViewModelTest {

    @get:Rule
    val dispatcherRule = CoroutineTestRule()

    private lateinit var viewModel: DogBreedsListViewModel
    private val useCase: DogBreedsListUseCase = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = DogBreedsListViewModel(useCase)
    }

    @Test
    fun `fetchRemoteDogBreeds should return success`() = runTest {
        // Given
        coEvery { useCase.fetchRemoteDogBreeds() } returns Result.success(true)

        // When
        viewModel.fetchRemoteDogBreeds()

        // Then
        advanceUntilIdle()
        viewModel.state.value.isSuccessRefreshing shouldBe true
    }
   /* @Test
    fun `fetchRemoteDogBreeds should update state on error`() = runTest {
        coEvery { useCase.fetchRemoteDogBreeds() } returns Result.Error(Exception("Network Error"))

        viewModel.fetchRemoteDogBreeds()

        viewModel.state.test {
            awaitItem().isErrorRefreshing shouldBe true
            awaitItem().errorMessage shouldBe "Network Error"
        }
    }

    @Test
    fun `fetchRemoteDogBreeds should update state on loading`() = runTest {
        coEvery { useCase.fetchRemoteDogBreeds() } returns Result.Loading

        viewModel.fetchRemoteDogBreeds()

        viewModel.state.test {
            awaitItem().isRefreshing shouldBe true
        }
    }

    @Test
    fun `getLocalDogBreeds should update state with dog breeds`() = runTest {
        // Given
        val dogBreeds = listOf(
            DogBreed(
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
                imageUrl = "",
                isFavourite = false
            )
        )

        every { useCase.getLocalDogBreeds() } returns flowOf(dogBreeds)

        // When
        viewModel.getLocalDogBreeds()
        advanceUntilIdle() // Ensures all coroutines finish

        // Then
        viewModel.state.value.dogs shouldBe dogBreeds
    }*/
}