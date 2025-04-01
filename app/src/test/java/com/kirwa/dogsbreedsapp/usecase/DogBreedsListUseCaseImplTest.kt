package com.kirwa.dogsbreedsapp.usecase

import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import com.kirwa.dogsbreedsapp.domain.usecase.dogBreedsList.DogBreedsListUseCaseImpl
import com.kirwa.dogsbreedsapp.utils.CoroutineTestRule
import io.kotest.matchers.shouldBe
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
 * This is the unit test class to test DogBreedsUseCase using Mockk, Junit, Kotest and Kotlinx.
 */
@ExperimentalCoroutinesApi
class DogBreedsListUseCaseImplTest {
    lateinit var dogBreedsUseCase: DogBreedsListUseCaseImpl

    private val dataRepository = mockk<DogBreedsRepository>()


    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        dogBreedsUseCase = DogBreedsListUseCaseImpl(dataRepository)
    }

    @Test
    fun `fetch dog breeds`() = runTest {
        // given
        val mockDogBreeds = mockk<List<DogBreed>>(relaxed = true)

        every {
            dataRepository.getLocalDogBreeds()
        } returns flow { emit(mockDogBreeds) }

        // when
        val useCaseValue =
            dogBreedsUseCase.getLocalDogBreeds()

        //then
        useCaseValue.first() shouldBe mockDogBreeds
    }
}