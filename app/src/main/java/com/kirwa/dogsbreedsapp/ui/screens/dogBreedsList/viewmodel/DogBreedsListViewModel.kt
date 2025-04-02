package com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.model.FavouriteUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.kirwa.dogsbreedsapp.data.remote.model.Result
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.DogBreedWithFavourite
import com.kirwa.dogsbreedsapp.domain.usecase.dogBreedsList.DogBreedsListUseCase
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.model.DogBreedsListUiState
import com.kirwa.dogsbreedsapp.utils.ConnectivityHelper

/**
 * ViewModel for managing dog breed list data.
 *
 * - Fetches remote and local dog breed data on initialization.
 * - Handles UI state updates based on API responses.
 *
 * Uses:
 * - `StateFlow` for UI state management.
 * - `viewModelScope` for coroutine handling.
 * - `Dispatchers.IO` for network operations.
 */
class DogBreedsListViewModel(
    private val useCase: DogBreedsListUseCase,
    private val connectivityHelper: ConnectivityHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(DogBreedsUiState())
    val uiState: StateFlow<DogBreedsUiState> = _uiState

    val dogBreeds: Flow<PagingData<DogBreedWithFavourite>> = useCase.getPagedDogBreeds()
        .cachedIn(viewModelScope)
        .onEach {
            _uiState.update {
                it.copy(isNetworkAvailable = connectivityHelper.isConnected())
            }
        }

    fun retry() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            dogBreeds.first() // Triggers refresh
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}



data class DogBreedsUiState(
    val isLoading: Boolean = false,
    val isNetworkAvailable: Boolean = true,
    val error: Throwable? = null
)

