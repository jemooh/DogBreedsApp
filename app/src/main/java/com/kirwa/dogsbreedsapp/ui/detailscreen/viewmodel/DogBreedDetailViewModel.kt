package com.kirwa.dogsbreedsapp.ui.detailscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import com.kirwa.dogsbreedsapp.ui.listscreens.model.DogBreedsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.kirwa.dogsbreedsapp.data.remote.model.Result
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed

class DogBreedDetailViewModel(private val dogBreedsRepository: DogBreedsRepository) :
    ViewModel() {
    private val _state = MutableStateFlow(DogBreedsState())
    val state: StateFlow<DogBreedsState> = _state


    fun getDogBreedById(id:Int) {
        dogBreedsRepository.getDogBreedById(id).onEach { dogBreed ->
            _state.value = state.value.copy(
                dog = dogBreed
            )
        }.launchIn(viewModelScope)
    }

    fun getFavouriteDogBreedById(id:Int) {
        dogBreedsRepository.getFavouriteDogBreedById(id).onEach { dogBreed ->
            _state.value = state.value.copy(
                favouriteDogBreed = dogBreed
            )
        }.launchIn(viewModelScope)
    }


    fun saveFavouriteDogBreed(dogBreed: FavouriteDogBreed) {
        viewModelScope.launch {
            dogBreedsRepository.saveFavouriteDogBreed(dogBreed)
        }
    }

    fun deleteFavouriteDogBreed(dogBreed: FavouriteDogBreed) {
        viewModelScope.launch {
            dogBreedsRepository.deleteFavouriteDogBreed(dogBreed)
        }
    }
}

