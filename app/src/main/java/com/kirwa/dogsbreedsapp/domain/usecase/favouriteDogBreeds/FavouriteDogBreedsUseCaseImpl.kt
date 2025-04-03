package com.kirwa.dogsbreedsapp.domain.usecase.favouriteDogBreeds

import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import kotlinx.coroutines.flow.Flow
/**
 * Implementation of the FavouriteDogBreedsUseCase interface.
 *
 * - Provides methods for interacting with the data repository to fetch, save, and delete favourite dog breeds.
 * - Exposes methods to get a list of local favourite dog breeds, save a new favourite breed, and delete a breed from favourites.
 *
 * @param dataRepository The repository responsible for data operations related to dog breeds.
 */
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