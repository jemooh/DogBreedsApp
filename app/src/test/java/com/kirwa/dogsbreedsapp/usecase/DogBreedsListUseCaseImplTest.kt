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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for [DogBreedsListUseCaseImpl].
 *
 * This test class verifies the behavior of the Dog Breeds List Use Case, ensuring that:
 * - The list of dog breeds is correctly retrieved from the local database.
 *
 * Test cases:
 * 1. `fetch dog breeds` - Ensures that a list of dog breeds is retrieved correctly.
 *
 * Uses:
 * - MockK for dependency mocking.
 * - Kotest assertions for validation.
 * - kotlinx.coroutines Test API for coroutine-based testing.
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class DogBreedsListUseCaseImplTest {
    private lateinit var useCase: DogBreedsListUseCaseImpl
    private val repository = mockk<DogBreedsRepository>()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        useCase = DogBreedsListUseCaseImpl(repository)
    }

    @Test
    fun `getPagedDogBreeds should return flow from repository`() = runTest {
        // Given
        val testData = listOf(
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
        )

        val pagingData = PagingData.from(testData)
        coEvery { repository.getPagedDogBreeds() } returns flowOf(pagingData)

        // When
        val result = useCase.getPagedDogBreeds().first()

        // Then
        val differ = AsyncPagingDataDiffer(
            diffCallback = object : DiffUtil.ItemCallback<DogBreedWithFavourite>() {
                override fun areItemsTheSame(
                    oldItem: DogBreedWithFavourite,
                    newItem: DogBreedWithFavourite
                ) = oldItem.dogBreed.id == newItem.dogBreed.id

                override fun areContentsTheSame(
                    oldItem: DogBreedWithFavourite,
                    newItem: DogBreedWithFavourite
                ) = oldItem == newItem
            },
            updateCallback = NoopListUpdateCallback(),
            mainDispatcher = coroutineTestRule.dispatcher,
            workerDispatcher = coroutineTestRule.dispatcher
        )

        val collectJob = launch {
            // Using regular collect instead of collectLatest
            /*result.collectLatest { pagingData ->
                differ.submitData(pagingData)
            }*/
        }

        advanceUntilIdle()

        // Verification
        differ.snapshot().items shouldContainExactly testData
        coVerify(exactly = 1) { repository.getPagedDogBreeds() }

        collectJob.cancel()
    }
}

// Required in test sources
class NoopListUpdateCallback : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}