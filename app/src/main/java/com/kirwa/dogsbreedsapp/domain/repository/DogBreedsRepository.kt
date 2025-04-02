package com.kirwa.dogsbreedsapp.domain.repository

import androidx.paging.PagingData
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import kotlinx.coroutines.flow.Flow
import com.kirwa.dogsbreedsapp.data.remote.model.Result
import com.kirwa.dogsbreedsapp.domain.model.DogBreedWithFavourite

interface DogBreedsRepository {
    fun getPagedDogBreeds(): Flow<PagingData<DogBreedWithFavourite>>
    fun getDogBreedById(id: Int): Flow<DogBreed>
    suspend fun deleteFavouriteDogBreed(dogId: Int)
    fun getLocalFavouriteDogBreeds(): Flow<List<FavouriteDogBreed>>
    suspend fun saveFavouriteDogBreed(dog: FavouriteDogBreed)
}