package com.kirwa.dogsbreedsapp.domain.usecase.favouriteDogBreeds

import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import kotlinx.coroutines.flow.Flow


interface FavouriteDogBreedsUseCase {
    fun getLocalFavouriteDogBreeds(): Flow<List<FavouriteDogBreed>>
    suspend fun deleteFavouriteDogBreed(dogId: Int)
    suspend fun saveFavouriteDogBreed(dog: FavouriteDogBreed)

}