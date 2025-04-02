package com.kirwa.dogsbreedsapp.usecase

import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import com.kirwa.dogsbreedsapp.domain.usecase.dogBreedDetails.DogBreedDetailUseCaseImpl
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
 * Unit tests for [DogBreedDetailUseCaseImpl].
 *
 * This test class verifies the behavior of the Dog Breed Detail Use Case, ensuring that:
 * - A specific dog breed can be retrieved by its ID.
 *
 * Test cases:
 * 1. `fetch dog breeds by id` - Ensures that a dog breed is correctly retrieved based on its ID.
 *
 * Uses:
 * - MockK for dependency mocking.
 * - Kotest assertions for validation.
 * - kotlinx.coroutines Test API for coroutine-based testing.
 */
@ExperimentalCoroutinesApi
class DogBreedDetailUseCaseImplTest {
    lateinit var dogBreedsUseCase: DogBreedDetailUseCaseImpl

    private val dataRepository = mockk<DogBreedsRepository>()


    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        dogBreedsUseCase = DogBreedDetailUseCaseImpl(dataRepository)
    }

    @Test
    fun `fetch dog breeds by id`() = runTest {
        // given
        val mockDogBreeds = mockk<DogBreed>(relaxed = true)

        every {
            dataRepository.getDogBreedById(3)
        } returns flow { emit(mockDogBreeds) }

        // when
        val useCaseValue =
            dogBreedsUseCase.getDogBreedById(3)

        //then
        useCaseValue.first() shouldBe mockDogBreeds
    }
}