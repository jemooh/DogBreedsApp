package com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import com.kirwa.dogsbreedsapp.domain.usecase.favouriteDogBreeds.FavouriteDogBreedsUseCase
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.model.DogBreedsListUiState
import com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.model.FavouriteUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavouriteDogBreedsViewModel(private val favouriteDogBreedsUseCase: FavouriteDogBreedsUseCase) :
    ViewModel() {

    private val _state = MutableStateFlow(DogBreedsListUiState())
    val state: StateFlow<DogBreedsListUiState> = _state

    init {
        getLocalFavouriteDogBreeds()
    }

    fun getLocalFavouriteDogBreeds() {
        favouriteDogBreedsUseCase.getLocalFavouriteDogBreeds().onEach { favorites ->
            _state.value = state.value.copy(
                favouriteDogBreeds = favorites
            )
        }.launchIn(viewModelScope)
    }

    fun saveFavouriteDogBreed(dogBreed: FavouriteDogBreed) {
        viewModelScope.launch {
            favouriteDogBreedsUseCase.saveFavouriteDogBreed(dogBreed)
        }
    }

    fun deleteFavouriteDogBreed(dogInt: Int) {
        viewModelScope.launch {
            favouriteDogBreedsUseCase.deleteFavouriteDogBreed(dogInt)
        }
    }
}

