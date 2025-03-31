package com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.model.FavouriteUiState
import kotlinx.coroutines.flow.*

class FavouriteDogBreedsViewModel(private val dogBreedsRepository: DogBreedsRepository) :
    ViewModel() {
    private val _state = MutableStateFlow(FavouriteUiState())
    val state: StateFlow<FavouriteUiState> = _state

    fun getLocalFavouriteDogBreeds() {
        dogBreedsRepository.getLocalFavouriteDogBreeds().onEach { favorites ->
            _state.value = state.value.copy(
                favouriteDogBreeds = favorites
            )
        }.launchIn(viewModelScope)
    }
}

