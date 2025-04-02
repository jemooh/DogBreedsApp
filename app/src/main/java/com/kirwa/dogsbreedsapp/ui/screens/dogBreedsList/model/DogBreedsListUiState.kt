package com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.model

import androidx.paging.PagingData
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed

data class DogBreedsListUiState(
    val dogs: PagingData<DogBreed> = PagingData.empty(), // Use PagingData instead of List
    val isRefreshing: Boolean = false,
    val isErrorRefreshing: Boolean = false,
    val errorMessage: String = "",
)