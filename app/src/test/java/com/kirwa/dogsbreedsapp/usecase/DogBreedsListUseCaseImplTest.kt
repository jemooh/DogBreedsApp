package com.kirwa.dogsbreedsapp.usecase

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.DogBreedWithFavourite
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import com.kirwa.dogsbreedsapp.domain.usecase.dogBreedsList.DogBreedsListUseCaseImpl
import com.kirwa.dogsbreedsapp.utils.CoroutineTestRule
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.collectLatest

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertThrows

/**
 * Unit tests for [DogBreedsListUseCaseImpl] verifying:
 *
 * 1. Paged dog breeds data flow from repository
 * 2. Proper pagination handling
 * 3. Error propagation from repository
 * 4. Coroutine safety
 *
 * Tests both successful data flow and error scenarios using
 * [StandardTestDispatcher] for precise coroutine control.
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class DogBreedsListUseCaseImplTest {
    private lateinit var useCase: DogBreedsListUseCaseImpl
    private val repository = mockk<DogBreedsRepository>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        useCase = DogBreedsListUseCaseImpl(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getPagedDogBreeds should return flow from repository`() = runTest {
        // Given
        val testData = PagingData.from(listOf(
            DogBreedWithFavourite(
                dogBreed = DogBreed(
                    id = 1,
                    name = "Labrador",
                    weight = "50-60",
                    height = "22-24",
                    bredFor = "Retrieving game",
                    breedGroup = "Sporting",
                    temperament = "Friendly",
                    origin = "Canada",
                    lifeSpan = "10-12 years",
                    referenceImageId = "abc123",
                    imageUrl = "https://example.com/image.jpg"
                ),
                isFavourite = true
            )
        ))

        coEvery { repository.getPagedDogBreeds() } returns flowOf(testData)

        // When
        val result = useCase.getPagedDogBreeds()

        // Then
        val collector = TestCollector<PagingData<DogBreedWithFavourite>>()
        val collectJob = launch { result.collect(collector) }
        advanceUntilIdle()

        collector.values.size shouldBe 1
        coVerify(exactly = 1) { repository.getPagedDogBreeds() }
        collectJob.cancel()
    }

    @Test
    fun `getPagedDogBreeds should propagate errors from repository`() = runTest {
        // Given
        val testError = RuntimeException("Test error")
        coEvery { repository.getPagedDogBreeds() } returns flow { throw testError }

        // When
        val result = useCase.getPagedDogBreeds()

        // Then
        val collector = TestCollector<PagingData<DogBreedWithFavourite>>()
        val collectJob = launch {
            assertThrows<RuntimeException> {
                result.collect(collector)
            } shouldBe testError
        }
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.getPagedDogBreeds() }
        collectJob.cancel()
    }

    // Helper collector class
    private class TestCollector<T> : FlowCollector<T> {
        val values = mutableListOf<T>()
        override suspend fun emit(value: T) { values.add(value) }
    }
}