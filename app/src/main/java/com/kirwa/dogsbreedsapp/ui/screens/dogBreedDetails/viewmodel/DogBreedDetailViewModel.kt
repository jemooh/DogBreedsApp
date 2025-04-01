package com.kirwa.dogsbreedsapp.ui.screens.dogBreedDetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.model.FavouriteUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedDetails.model.DogBreedDetailsUiState

class DogBreedDetailViewModel(private val dogBreedsRepository: DogBreedsRepository) :
    ViewModel() {
    private val _state = MutableStateFlow(DogBreedDetailsUiState())
    val state: StateFlow<DogBreedDetailsUiState> = _state

    fun getDogBreedById(id:Int) {
        dogBreedsRepository.getDogBreedById(id).onEach { dogBreed ->
            _state.value = state.value.copy(
                dog = dogBreed
            )
        }.launchIn(viewModelScope)
    }

    fun saveFavouriteDogBreed(dogBreed: FavouriteDogBreed) {
        viewModelScope.launch {
            dogBreedsRepository.saveFavouriteDogBreed(dogBreed)
        }
    }

    fun deleteFavouriteDogBreed(dogInt: Int) {
        viewModelScope.launch {
            dogBreedsRepository.deleteFavouriteDogBreed(dogInt)
        }
    }
}

