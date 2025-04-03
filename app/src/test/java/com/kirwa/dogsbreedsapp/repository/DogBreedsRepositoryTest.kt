package com.kirwa.dogsbreedsapp.repository

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.recyclerview.widget.DiffUtil
import app.cash.turbine.test
import com.kirwa.dogsbreedsapp.data.local.dao.DogBreedsDao
import com.kirwa.dogsbreedsapp.data.local.dao.FavouriteDogBreedsDao
import com.kirwa.dogsbreedsapp.data.local.dao.RemoteKeyDao
import com.kirwa.dogsbreedsapp.data.remote.apiService.DogsApiService
import com.kirwa.dogsbreedsapp.data.repository.DogBreedsRepositoryImpl
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.kirwa.dogsbreedsapp.domain.model.DogBreedWithFavourite
import com.kirwa.dogsbreedsapp.utils.NoopListUpdateCallback
import com.kirwa.dogsbreedsapp.utils.dogBreedTest
import com.kirwa.dogsbreedsapp.utils.favouriteDogBreedTest
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle

/**
 * Unit tests for [DogBreedsRepositoryImpl] verifying:
 *
 * 1. Paged dog breeds data flow from local DAO
 * 2. Single dog breed retrieval
 * 3. Favorites management operations:
 *    - Saving favorites
 *    - Deleting favorites
 *    - Retrieving favorites list
 *
 * Uses MockK for mocking dependencies and TestCoroutineDispatcher for coroutine testing.
 * Tests cover both success scenarios and proper delegation to underlying DAOs.
 *
 * @see DogBreedsRepositoryImpl
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class DogBreedsRepositoryImplTest {

    private lateinit var repository: DogBreedsRepositoryImpl
    private val dogsApiService: DogsApiService = mockk()
    private val dogBreedsDao: DogBreedsDao = mockk(relaxed = true)
    private val favouriteDogBreedsDao: FavouriteDogBreedsDao = mockk(relaxed = true)
    private val remoteKeyDao: RemoteKeyDao = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    private val dogBreedWithFavouriteTest = DogBreedWithFavourite(
        dogBreed = dogBreedTest,
        isFavourite = true
    )


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = DogBreedsRepositoryImpl(
            dogsApiService,
            dogBreedsDao,
            favouriteDogBreedsDao,
            remoteKeyDao,
            testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getPagedDogBreeds should return paging data from DAO`() = runTest {
        // Given
        val testData = listOf(dogBreedWithFavouriteTest)
        val mockPagingSource = mockk<PagingSource<Int, DogBreedWithFavourite>>(relaxed = true)

        coEvery { dogBreedsDao.getPagedDogBreeds() } returns mockPagingSource
        coEvery { mockPagingSource.load(any()) } returns PagingSource.LoadResult.Page(
            data = testData,
            prevKey = null,
            nextKey = null
        )

        // When
        val flow = repository.getPagedDogBreeds()
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
            mainDispatcher = testDispatcher,
            workerDispatcher = testDispatcher
        )

        val collectJob = launch {
            flow.collect { pagingData ->
                differ.submitData(pagingData)
            }
        }

        // Wait for data to be loaded
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { dogBreedsDao.getPagedDogBreeds() }
        differ.snapshot().items shouldContainExactly testData

        collectJob.cancel()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `getPagedDogBreeds should call DAO method`() = runTest {
        // 1. Create a properly mocked PagingSource
        val mockPagingSource = mockk<PagingSource<Int, DogBreedWithFavourite>>(relaxUnitFun = true) {
            coEvery { load(any()) } returns PagingSource.LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        }

        // 2. Setup DAO mock
        coEvery { dogBreedsDao.getPagedDogBreeds() } returns mockPagingSource

        // 3. Execute and collect
        val flow = repository.getPagedDogBreeds()
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            flow.collect {
                // Just collect to trigger execution
            }
        }

        // 4. Let coroutines execute
        advanceUntilIdle()

        // 5. Verify
        coVerify(exactly = 1) { dogBreedsDao.getPagedDogBreeds() }

        // 6. Clean up
        collectJob.cancel()
    }

    @Test
    fun `getDogBreedById should return flow from DAO`() = runTest {
        // Given
        every { dogBreedsDao.getDogBreedById(1) } returns flowOf(dogBreedTest)

        // When
        val result = repository.getDogBreedById(1)

        // Then
        result.test {
            awaitItem() shouldBe dogBreedTest
            cancelAndConsumeRemainingEvents()
        }
        verify { dogBreedsDao.getDogBreedById(1) }
    }

    @Test
    fun `deleteFavouriteDogBreed should call DAO delete method`() = runTest {
        // Given
        coEvery { favouriteDogBreedsDao.deleteFavouriteDogBreedById(1) } just Runs

        // When
        repository.deleteFavouriteDogBreed(1)

        // Then
        coVerify(exactly = 1) { favouriteDogBreedsDao.deleteFavouriteDogBreedById(1) }
    }

    @Test
    fun `getLocalFavouriteDogBreeds should return flow from DAO`() = runTest {
        // Given
        val favBreeds = listOf(favouriteDogBreedTest)
        every { favouriteDogBreedsDao.getFavouriteDogBreeds() } returns flowOf(favBreeds)

        // When
        val result = repository.getLocalFavouriteDogBreeds()

        // Then
        result.test {
            awaitItem() shouldBe favBreeds
            cancelAndConsumeRemainingEvents()
        }
        verify { favouriteDogBreedsDao.getFavouriteDogBreeds() }
    }

    @Test
    fun `saveFavouriteDogBreed should call DAO insert method`() = runTest {
        // Given
        coEvery { favouriteDogBreedsDao.insertAsync(favouriteDogBreedTest) } just Runs

        // When
        repository.saveFavouriteDogBreed(favouriteDogBreedTest)

        // Then
        coVerify(exactly = 1) { favouriteDogBreedsDao.insertAsync(favouriteDogBreedTest) }
    }
}

