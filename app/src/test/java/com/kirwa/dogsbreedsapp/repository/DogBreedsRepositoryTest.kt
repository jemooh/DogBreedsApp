package com.kirwa.dogsbreedsapp.repository

import app.cash.turbine.test
import com.kirwa.dogsbreedsapp.data.local.dao.DogBreedsDao
import com.kirwa.dogsbreedsapp.data.local.dao.FavouriteDogBreedsDao
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
import com.kirwa.dogsbreedsapp.utils.dogBreedTest
import com.kirwa.dogsbreedsapp.utils.favouriteDogBreedTest
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import kotlinx.coroutines.flow.flowOf
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
class DogBreedsRepositoryImplTest {

    private lateinit var repository: DogBreedsRepositoryImpl
    private val dogsApiService: DogsApiService = mockk()
    private val dogBreedsDao: DogBreedsDao = mockk(relaxed = true)
    private val favouriteDogBreedsDao: FavouriteDogBreedsDao = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = DogBreedsRepositoryImpl(dogsApiService, dogBreedsDao, favouriteDogBreedsDao, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchRemoteDogBreeds should return success when API call is successful`() = runTest {
        // Given
        val mockResponse = listOf(
            DogBreedsResponse(
                id = 1, name = "Labrador", weight = Weight("50", "22"),
                height = Height("24", "60"), breedGroup = "Sporting",
                lifeSpan = "10-12 years", temperament = "Friendly",
                referenceImageId = "abc123", image = Image(id = "abc123", url = "https://example.com/image.jpg")
            )
        )
        coEvery { dogsApiService.fetchDogBreeds(any(), any()) } returns Response.success(mockResponse)

        // When
        val result = repository.fetchRemoteDogBreeds()

        // Then
        result shouldBe Result.Success(true)

        coVerify { dogBreedsDao.insertAsync(any<DogBreed>()) }
    }

    @Test
    fun `fetchRemoteDogBreeds should return error when API call fails`() = runTest {
        // Given
        coEvery { dogsApiService.fetchDogBreeds(any(), any()) } returns Response.error(500, "Server Error".toResponseBody())

        // When
        val result = repository.fetchRemoteDogBreeds()

        // Then
        result shouldBe Result.Success(false)
    }

    @Test
    fun `fetchRemoteDogBreeds should return error on network failure`() = runTest {
        // Given
        coEvery { dogsApiService.fetchDogBreeds(any(), any()) } throws IOException("Network error")

        // When
        val result = repository.fetchRemoteDogBreeds()

        // Then
        when (result) {
            is Result.Error -> result.exception.message shouldBe "Error Occurred"
            else -> fail("Expected Result.Error but got $result")
        }
    }


    @Test
    fun `getLocalDogBreeds returns data from database`() = runTest {
        // Given
        val localBreeds = listOf(dogBreedTest)
        coEvery { dogBreedsDao.getDogBreeds() } returns flowOf(localBreeds)

        // When
        val result = repository.getLocalDogBreeds().first()

        // Then
        result shouldBe localBreeds
    }

    @Test
    fun `getDogBreedById should return correct dog breed`() = runTest {
        // Given
        every { dogBreedsDao.getDogBreedById(1) } returns flowOf(dogBreedTest)

        // When
        val result = repository.getDogBreedById(1)

        // Then
        result.test {
            awaitItem() shouldBe dogBreedTest
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `deleteFavouriteDogBreed should call DAO method`() = runTest {
        // Given

        coEvery { favouriteDogBreedsDao.deleteFavouriteDogBreedById(1) } just Runs

        // When
        repository.deleteFavouriteDogBreed(1)

        // Then
        coVerify(exactly = 1) { favouriteDogBreedsDao.deleteFavouriteDogBreedById(1) }
    }

    @Test
    fun `getLocalFavouriteDogBreeds should return list of favourite breeds`() = runTest {
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
    }

    @Test
    fun `saveFavouriteDogBreed should insert favourite breed`() = runTest {
        // Given
        val favouriteDogBreedTest = favouriteDogBreedTest
        coEvery { favouriteDogBreedsDao.insertAsync(favouriteDogBreedTest) } just Runs

        // When
        repository.saveFavouriteDogBreed(favouriteDogBreedTest)

        // Then
        coVerify(exactly = 1) { favouriteDogBreedsDao.insertAsync(favouriteDogBreedTest) }
    }
}
