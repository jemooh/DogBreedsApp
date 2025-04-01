package com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.model.FavouriteUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.kirwa.dogsbreedsapp.data.remote.model.Result
import com.kirwa.dogsbreedsapp.domain.usecase.dogBreedsList.DogBreedsListUseCase
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.model.DogBreedsListUiState

class DogBreedsListViewModel(private val dogBreedsListUseCase: DogBreedsListUseCase) :
    ViewModel() {
    private val _state = MutableStateFlow(DogBreedsListUiState())
    val state: StateFlow<DogBreedsListUiState> = _state

    init {
        fetchRemoteDogBreeds()
        getLocalDogBreeds()
    }

    fun fetchRemoteDogBreeds() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = dogBreedsListUseCase.fetchRemoteDogBreeds()) {
                is Result.Loading -> {
                    _state.value = state.value.copy(
                        isRefreshing = true
                    )
                }

                is Result.Success -> {
                    _state.value = state.value.copy(
                        isSuccessRefreshing = true
                    )
                }

                is Result.Error -> {
                    _state.value = state.value.copy(
                        isErrorRefreshing = true,
                        errorMessage = result.exception.message.toString()
                    )
                }
                else -> {}
            }
        }
    }

    fun getLocalDogBreeds() {
        dogBreedsListUseCase.getLocalDogBreeds()
            .onEach { dogBreeds ->
                _state.value = state.value.copy(
                    dogs = dogBreeds
                )
            }.launchIn(viewModelScope)
    }

}

