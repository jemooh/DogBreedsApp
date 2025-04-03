package com.kirwa.dogsbreedsapp.viewmodel.dogBreedDetail

import com.kirwa.dogsbreedsapp.domain.usecase.dogBreedDetails.DogBreedDetailUseCase
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedDetails.viewmodel.DogBreedDetailViewModel
import com.kirwa.dogsbreedsapp.utils.CoroutineTestRule
import com.kirwa.dogsbreedsapp.utils.dogBreedTest
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test


/**
 * Unit tests for [DogBreedDetailViewModel] verifying:
 *
 * 1. Successful loading of dog breed details by ID
 * 2. Proper state updates when receiving breed data
 * 3. Correct interaction with [DogBreedDetailUseCase]
 *
 * Uses MockK for use case mocking and TestCoroutineDispatcher
 * for predictable coroutine execution.
 */
@ExperimentalCoroutinesApi
class DogBreedDetailViewModelTest {

    // Mocks
    private val dogBreedDetailUseCase: DogBreedDetailUseCase = mockk()
    private lateinit var viewModel: DogBreedDetailViewModel

    // Test coroutine dispatcher
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        viewModel = DogBreedDetailViewModel(dogBreedDetailUseCase)
    }

    @Test
    fun `getDogBreedById should update state with dog breed`(): Unit = runTest {
        // Given
        val dogBreed = dogBreedTest

        val flow = flowOf(dogBreed)
        every { dogBreedDetailUseCase.getDogBreedById(5) } returns flow

        // When
        viewModel.getDogBreedById(5)

        // Then
        viewModel.state.value.dog shouldBe dogBreed
    }
}
