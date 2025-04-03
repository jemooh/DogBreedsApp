package com.kirwa.dogsbreedsapp.viewmodel.dogBreedList

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.kirwa.dogsbreedsapp.domain.usecase.dogBreedsList.DogBreedsListUseCase
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.viewmodel.DogBreedsListViewModel
import com.kirwa.dogsbreedsapp.utils.CoroutineTestRule
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import com.kirwa.dogsbreedsapp.data.remote.model.Result
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.DogBreedWithFavourite
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.viewmodel.DogBreedsUiState
import com.kirwa.dogsbreedsapp.utils.ConnectivityHelper
import com.kirwa.dogsbreedsapp.utils.dogBreedTest
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainInOrder
import kotlinx.coroutines.test.setMain
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for [DogBreedsListViewModel] verifying:
 *
 * 1. Initial state setup
 * 2. Paged dog breeds data flow
 * 3. Network connectivity status updates
 * 4. Retry mechanism behavior
 * 5. Loading state transitions
 *
 * Tests both UI state management and data flow using
 * [StandardTestDispatcher] for precise coroutine control.
 * Validates integration with [DogBreedsListUseCase] and
 * [ConnectivityHelper] dependencies.
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class DogBreedsListViewModelTest {
    private lateinit var viewModel: DogBreedsListViewModel
    private val useCase = mockk<DogBreedsListUseCase>()
    private val connectivityHelper = mockk<ConnectivityHelper>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Default mocks
        coEvery { useCase.getPagedDogBreeds() } returns flowOf(PagingData.empty())
        every { connectivityHelper.isConnected() } returns true

        viewModel = DogBreedsListViewModel(useCase, connectivityHelper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState should have default values`() = runTest(testDispatcher) {
        viewModel.uiState.value shouldBe DogBreedsUiState(
            isLoading = false,
            isNetworkAvailable = true,
            error = null
        )
    }

    @Test
    fun `dogBreeds flow should update network status`() = runTest(testDispatcher) {
        // Given
        val testData = PagingData.from(listOf(
            DogBreedWithFavourite(
                dogBreed = dogBreedTest,
                isFavourite = true
            )
        ))
        coEvery { useCase.getPagedDogBreeds() } returns flowOf(testData)
        every { connectivityHelper.isConnected() } returns false

        // When
        val collector = TestCollector<PagingData<DogBreedWithFavourite>>()
        val collectJob = launch { viewModel.dogBreeds.collect(collector) }

        // Execute all pending coroutines
        advanceUntilIdle()

        // Then
        viewModel.uiState.value.isNetworkAvailable shouldBe false
        collectJob.cancel()
    }

    @Test
    fun `retry should update loading state`() = runTest(testDispatcher) {
        // Given
        val testData = PagingData.empty<DogBreedWithFavourite>()
        coEvery { useCase.getPagedDogBreeds() } returns flowOf(testData)

        // Start collecting states BEFORE triggering the action
        val states = mutableListOf<DogBreedsUiState>()
        val stateJob = launch { viewModel.uiState.collect(states::add) }

        // Initial state
        advanceUntilIdle()

        // When
        viewModel.retry()
        advanceUntilIdle()

        // Then
        states shouldContainInOrder listOf(
            DogBreedsUiState(isLoading = false), // initial state
            DogBreedsUiState(isLoading = true),  // retry started
            DogBreedsUiState(isLoading = false)   // retry completed
        )

        stateJob.cancel()
    }

    class TestCollector<T> : FlowCollector<T> {
        val values = mutableListOf<T>()
        override suspend fun emit(value: T) { values.add(value) }
    }
}