package com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.model

import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed

data class FavouriteUiState (
    val favouriteDogBreeds: List<FavouriteDogBreed> = emptyList(),
    val isLoading: Boolean = false,
)