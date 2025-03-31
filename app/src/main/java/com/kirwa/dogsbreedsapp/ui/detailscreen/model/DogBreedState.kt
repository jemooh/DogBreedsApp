package com.kirwa.dogsbreedsapp.ui.detailscreen.model

import com.kirwa.dogsbreedsapp.domain.model.DogBreed

data class DogBreedState (
    val dog: DogBreed? = null,
    val isLoading: Boolean = false
)