package com.kirwa.dogsbreedsapp.domain.repository

import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import kotlinx.coroutines.flow.Flow
import com.kirwa.dogsbreedsapp.data.remote.model.Result

interface DogBreedsRepository {
    suspend fun fetchRemoteDogBreeds(): Result<Boolean>
    fun getLocalDogBreeds(): Flow<List<DogBreed>>
    fun getDogBreedById(id: Int): Flow<DogBreed>
    fun getFavouriteDogBreedById(id: Int): Flow<FavouriteDogBreed>
    fun getLocalFavouriteDogBreeds(): Flow<List<FavouriteDogBreed>>

    fun searchLocalDogBreeds(query: String): Flow<List<DogBreed>>
    fun searchLocalFavouriteDogBreeds(query: String): Flow<List<FavouriteDogBreed>>
    suspend fun saveFavouriteDogBreed(dog: FavouriteDogBreed)
    suspend fun deleteFavouriteDogBreed(dog: FavouriteDogBreed)}
