package com.kirwa.dogsbreedsapp.domain.usecase.dogBreedsList

import androidx.paging.PagingData
import com.kirwa.dogsbreedsapp.data.remote.model.Result
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.DogBreedWithFavourite
import kotlinx.coroutines.flow.Flow


interface DogBreedsListUseCase {
    fun getPagedDogBreeds(): Flow<PagingData<DogBreedWithFavourite>>

}