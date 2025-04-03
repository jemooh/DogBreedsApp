package com.kirwa.dogsbreedsapp.ui.screens.dogBreedDetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.model.FavouriteUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import com.kirwa.dogsbreedsapp.domain.usecase.dogBreedDetails.DogBreedDetailUseCase
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedDetails.model.DogBreedDetailsUiState
/**
 * ViewModel for dog breed details screen that:
 *
 * 1. Manages UI state for a single dog breed
 * 2. Coordinates data fetching via [DogBreedDetailUseCase]
 * 3. Provides reactive state through [StateFlow]
 *
 * Key Features:
 * - Loads breed details by ID on demand
 * - Maintains current state in [DogBreedDetailsUiState]
 * - Uses coroutines for async operations
 *
 * @param dogBreedDetailUseCase The use case for fetching breed details
 */
class DogBreedDetailViewModel(private val dogBreedDetailUseCase: DogBreedDetailUseCase) :
    ViewModel() {
    private val _state = MutableStateFlow(DogBreedDetailsUiState())
    val state: StateFlow<DogBreedDetailsUiState> = _state

    fun getDogBreedById(id:Int) {
        dogBreedDetailUseCase.getDogBreedById(id).onEach { dogBreed ->
            _state.value = state.value.copy(
                dog = dogBreed
            )
        }.launchIn(viewModelScope)
    }

}

