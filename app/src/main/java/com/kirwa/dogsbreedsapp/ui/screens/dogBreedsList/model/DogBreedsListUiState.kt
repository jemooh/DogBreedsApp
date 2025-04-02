package com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.model

import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed

data class DogBreedsListUiState (
    val dogs: List<DogBreed> = emptyList(),
    val isRefreshing: Boolean = false,
    val isErrorRefreshing: Boolean = false,
    val errorMessage: String = "",
)