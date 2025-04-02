package com.kirwa.dogsbreedsapp.usecase

import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import com.kirwa.dogsbreedsapp.domain.usecase.favouriteDogBreeds.FavouriteDogBreedsUseCaseImpl
import com.kirwa.dogsbreedsapp.utils.CoroutineTestRule
import com.kirwa.dogsbreedsapp.utils.favouriteDogBreedTest
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for [FavouriteDogBreedsUseCaseImpl].
 *
 * This test class verifies the behavior of the Favourite Dog Breeds Use Case, ensuring that:
 * - Favorite dog breeds can be retrieved from the local database.
 * - Dog breeds can be saved as favorites correctly.
 *
 * Test cases:
 * 1. `fetch favourite dog breeds` - Ensures that favorite dog breeds are retrieved correctly.
 * 2. `Save to favourites` - Ensures that a dog breed can be saved as a favorite.
 *
 * Uses:
 * - MockK for dependency mocking.
 * - Kotest assertions for validation.
 * - kotlinx.coroutines Test API for coroutine-based testing.
 */
@ExperimentalCoroutinesApi
class FavouriteDogBreedsUseCaseImplTest {
    lateinit var favouriteDogBreedsUseCase: FavouriteDogBreedsUseCaseImpl

    private val dataRepository = mockk<DogBreedsRepository>()


    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        favouriteDogBreedsUseCase = FavouriteDogBreedsUseCaseImpl(dataRepository)
    }

    @Test
    fun `fetch favourite dog breeds`() = runTest {
        // given
        val mockFavouriteDogBreeds = mockk<List<FavouriteDogBreed>>(relaxed = true)

        every {
            dataRepository.getLocalFavouriteDogBreeds()
        } returns flow { emit(mockFavouriteDogBreeds) }

        // when
        val getFavouriteDogBreedsUseCaseValue =
            favouriteDogBreedsUseCase.getLocalFavouriteDogBreeds()

        //then
        getFavouriteDogBreedsUseCaseValue.first() shouldBe mockFavouriteDogBreeds
    }

    @Test
    fun `Save to favourites`() = runTest {
        // given
        val breed = favouriteDogBreedTest

        coEvery {
            dataRepository.saveFavouriteDogBreed(breed)
        } returns Unit

        // when
        val getFavouriteDogBreedsUseCaseValue =
            favouriteDogBreedsUseCase.saveFavouriteDogBreed(breed)

        //then
        getFavouriteDogBreedsUseCaseValue shouldBe Unit
    }
}