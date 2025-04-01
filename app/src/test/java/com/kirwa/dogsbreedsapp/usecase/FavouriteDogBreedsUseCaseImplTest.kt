package com.kirwa.dogsbreedsapp.usecase

import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import com.kirwa.dogsbreedsapp.domain.usecase.favouriteDogBreeds.FavouriteDogBreedsUseCaseImpl
import com.kirwa.dogsbreedsapp.utils.CoroutineTestRule
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
 * This is the unit test class to test FavouriteDogBreedsUseCase using Mockk, Junit, Kotest and Kotlinx.
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