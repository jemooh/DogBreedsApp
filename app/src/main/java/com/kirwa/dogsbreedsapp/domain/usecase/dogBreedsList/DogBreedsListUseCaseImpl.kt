package com.kirwa.dogsbreedsapp.domain.usecase.dogBreedsList

import androidx.paging.PagingData
import com.kirwa.dogsbreedsapp.data.remote.model.Result
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.DogBreedWithFavourite
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import kotlinx.coroutines.flow.Flow
/**
 * Implementation of the DogBreedsListUseCase interface.
 *
 * - Provides a method for retrieving paginated dog breeds along with their favourite status.
 * - Fetches the paginated data using the DogBreedsRepository.
 *
 * @param dataRepository The repository responsible for data operations related to dog breeds.
 */
class DogBreedsListUseCaseImpl(
    private val dataRepository: DogBreedsRepository
) : DogBreedsListUseCase {
    override fun getPagedDogBreeds(): Flow<PagingData<DogBreedWithFavourite>> {
        return dataRepository.getPagedDogBreeds()
    }
}