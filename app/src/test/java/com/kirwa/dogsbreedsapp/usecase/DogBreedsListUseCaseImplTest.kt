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