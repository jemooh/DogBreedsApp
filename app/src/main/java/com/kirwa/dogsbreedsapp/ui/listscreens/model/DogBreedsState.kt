package com.kirwa.dogsbreedsapp.ui.listscreens.model

import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed

data class DogBreedsState (
    val dogs: List<DogBreed> = emptyList(),
    val favouriteDogBreeds: List<FavouriteDogBreed> = emptyList(),
    val dog: DogBreed? = null,
    val favouriteDogBreed: FavouriteDogBreed? = null,
    val isSuccessRefreshing: Boolean = false,
    val isRefreshing: Boolean = false,
    val isErrorRefreshing: Boolean = false,
    val errorMessage: String = "",
)