package com.kirwa.dogsbreedsapp.domain.repository

import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import kotlinx.coroutines.flow.Flow
import com.kirwa.dogsbreedsapp.data.remote.model.Result

interface DogBreedsRepository {
    suspend fun fetchRemoteDogBreeds(): Result<Boolean>
    fun getLocalDogBreeds(): Flow<List<DogBreed>>
    fun getDogBreedById(id: Int): Flow<DogBreed>
    suspend fun deleteFavouriteDogBreed(dogId: Int)
    fun getLocalFavouriteDogBreeds(): Flow<List<FavouriteDogBreed>>
    suspend fun saveFavouriteDogBreed(dog: FavouriteDogBreed)
}