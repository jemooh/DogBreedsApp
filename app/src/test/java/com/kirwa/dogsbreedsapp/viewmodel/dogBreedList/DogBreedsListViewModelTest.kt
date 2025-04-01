package com.kirwa.dogsbreedsapp.viewmodel.dogBreedList

import app.cash.turbine.test
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.usecase.dogBreedsList.DogBreedsListUseCase
import com.kirwa.dogsbreedsapp.domain.usecase.dogBreedsList.DogBreedsListUseCaseImpl
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.viewmodel.DogBreedsListViewModel
import com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.viewmodel.FavouriteDogBreedsViewModel
import com.kirwa.dogsbreedsapp.utils.CoroutineTestRule
import io.kotest.core.test.TestStatus
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import com.kirwa.dogsbreedsapp.data.remote.model.Result
import io.mockk.MockKAnnotations

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions
import org.spekframework.spek2.Spek
import kotlin.time.ExperimentalTime


/**
 * This is the test class to test DogBreedsListViewModel using Mockk, Junit, Kotest and Kotlinx.
 */


@ExperimentalTime
@ExperimentalCoroutinesApi
internal class  DogBreedsListViewModelTest : Spek({
    val dogBreedsListUseCase = mockk<DogBreedsListUseCase>()
    val dogBreedsListViewModel by lazy { DogBreedsListViewModel(dogBreedsListUseCase = dogBreedsListUseCase) }

    val dispatcher = TestCoroutineDispatcher()

    beforeGroup {
        Dispatchers.setMain(dispatcher = dispatcher)
    }

    group("Fetching Dog Breeds Items") {

        test("Assert that an event was received and return it") {

            runBlocking {
                coEvery {
                    dogBreedsListUseCase.fetchRemoteDogBreeds()
                } returns Result.Success(
                    true
                )
                dogBreedsListViewModel.fetchRemoteDogBreeds()
                coVerify { dogBreedsListUseCase.fetchRemoteDogBreeds() }
                dogBreedsListViewModel.state.test {
                    awaitEvent()
                }
            }
        }
    }

    afterGroup {
        Dispatchers.resetMain()
    }
})


/*@ExperimentalCoroutinesApi
class DogBreedsListViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var viewModel: DogBreedsListViewModel

    private val dogBreedsUseCase = mockk<DogBreedsListUseCase>(relaxed = true, relaxUnitFun = true)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `fetchRemoteDogBreeds should update state on success`() = runTest {
        // Given: Mock successful fetch
        coEvery { dogBreedsUseCase.fetchRemoteDogBreeds() } returns Result.Success(true)

        // When
        viewModel = DogBreedsListViewModel(dogBreedsUseCase)

        viewModel.state.test {
            assertEquals(awaitItem().isSuccessRefreshing, true)
        }
    }

    @Test
    fun `fetchRemoteDogBreeds should update state on failure`() = runTest {
        // Given: Mock failure fetch
        coEvery { dogBreedsUseCase.fetchRemoteDogBreeds() } returns Result.Error(Exception("Network error"))

        // When
        viewModel = DogBreedsListViewModel(dogBreedsUseCase)

        viewModel.state.test {
            assertEquals(awaitItem().isErrorRefreshing, true)
        }
    }

    @Test
    fun `getLocalDogBreeds should return local breeds`() = runTest {
        // Given
        val breed = DogBreed(
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
        every { dogBreedsUseCase.getLocalDogBreeds() } returns flowOf(listOf(breed, breed))

        // When
        viewModel = DogBreedsListViewModel(dogBreedsUseCase)

        viewModel.state.test {
            assertEquals(awaitItem().dogs.size, 2)
        }
    }

 *//*   @Test
    fun `initialize then fetch dog breeds failed`() = runTest {
        // given
        every { dogBreedsUseCase.getLocalDogBreeds() } returns flow {
            emit(emptyList())
        }

        // when
        // view model is initialized
        viewModel = DogBreedsListViewModel(dogBreedsUseCase)

        // then
        val state = viewModel.state.first()
        state.dogs.size shouldBe 0
        state.isRefreshing shouldBe false
    }*//*
}*/
