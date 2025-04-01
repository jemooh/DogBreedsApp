package com.kirwa.dogsbreedsapp.domain.usecase.favouriteDogBreeds

import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import kotlinx.coroutines.flow.Flow

class FavouriteDogBreedsUseCaseImpl(
    private val dataRepository: DogBreedsRepository
) : FavouriteDogBreedsUseCase {
    override fun getLocalFavouriteDogBreeds(): Flow<List<FavouriteDogBreed>> {
        return dataRepository.getLocalFavouriteDogBreeds()
    }

    override suspend fun deleteFavouriteDogBreed(dogId: Int) {
        dataRepository.deleteFavouriteDogBreed(dogId)
    }

    override suspend fun saveFavouriteDogBreed(dog: FavouriteDogBreed) {
        dataRepository.saveFavouriteDogBreed(dog)
    }

}