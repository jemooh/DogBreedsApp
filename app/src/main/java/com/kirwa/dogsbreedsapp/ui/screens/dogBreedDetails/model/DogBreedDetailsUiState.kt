package com.kirwa.dogsbreedsapp.ui.screens.dogBreedDetails.model

import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed

data class DogBreedDetailsUiState (
    val dog: DogBreed? = null,
    val isLoading: Boolean = false
)