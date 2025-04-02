package com.kirwa.dogsbreedsapp.repository

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.test
import com.kirwa.dogsbreedsapp.data.local.dao.DogBreedsDao
import com.kirwa.dogsbreedsapp.data.local.dao.FavouriteDogBreedsDao
import com.kirwa.dogsbreedsapp.data.local.dao.RemoteKeyDao
import com.kirwa.dogsbreedsapp.data.remote.apiService.DogsApiService
import com.kirwa.dogsbreedsapp.data.remote.model.DogBreedsResponse
import com.kirwa.dogsbreedsapp.data.remote.model.Height
import com.kirwa.dogsbreedsapp.data.remote.model.Image
import com.kirwa.dogsbreedsapp.data.remote.model.Weight
import com.kirwa.dogsbreedsapp.data.repository.DogBreedsRepositoryImpl
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import io.kotest.core.test.TestStatus
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import okio.IOException
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import com.kirwa.dogsbreedsapp.data.remote.model.Result
import com.kirwa.dogsbreedsapp.domain.model.DogBreedWithFavourite
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import com.kirwa.dogsbreedsapp.utils.DogBreedDiffCallback
import com.kirwa.dogsbreedsapp.utils.NoopListUpdateCallback
import com.kirwa.dogsbreedsapp.utils.TestPagingSource
import com.kirwa.dogsbreedsapp.utils.dogBreedTest
import com.kirwa.dogsbreedsapp.utils.favouriteDogBreedTest
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import okhttp3.ResponseBody.Companion.toResponseBody
import kotlin.test.DefaultAsserter.fail

/**
 * Unit tests for [DogBreedsRepositoryImpl].
 *
 * This test class verifies the behavior of the repository methods by mocking the dependencies,
 * ensuring that:
 * - Remote data is fetched correctly from the API.
 * - Data is stored and retrieved correctly from the local database.
 * - Errors and exceptions are handled properly.
 * - DAO interactions are verified.
 *
 * Test cases:
 * 1. `fetchRemoteDogBreeds` - Ensures successful API response and error handling.
 * 2. `getLocalDogBreeds` - Ensures local breeds are retrieved correctly.
 * 3. `getDogBreedById` - Ensures a single breed is fetched correctly by ID.
 * 4. `deleteFavouriteDogBreed` - Ensures deletion of a favorite dog breed.
 * 5. `getLocalFavouriteDogBreeds` - Ensures retrieval of favorite dog breeds.
 * 6. `saveFavouriteDogBreed` - Ensures insertion of a favorite dog breed.
 *
 * Uses:
 * - MockK for mocking dependencies.
 * - Kotest assertions for validation.
 * - kotlinx.coroutines Test API for coroutine testing.
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

