package com.kirwa.dogsbreedsapp.domain.usecase.dogBreedsList

import com.kirwa.dogsbreedsapp.data.remote.model.Result
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import kotlinx.coroutines.flow.Flow


interface DogBreedsListUseCase {
    suspend fun fetchRemoteDogBreeds(): Result<Boolean>
    fun getLocalDogBreeds(): Flow<List<DogBreed>>
}