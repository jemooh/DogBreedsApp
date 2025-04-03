package com.kirwa.dogsbreedsapp.domain.usecase.dogBreedDetails

import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import kotlinx.coroutines.flow.Flow
/**
 * Implementation of the DogBreedDetailUseCase interface.
 *
 * - Provides a method for retrieving details of a dog breed by its ID.
 * - Fetches the dog breed details using the DogBreedsRepository.
 *
 * @param dataRepository The repository responsible for data operations related to dog breeds.
 */
class DogBreedDetailUseCaseImpl(
    private val dataRepository: DogBreedsRepository
) : DogBreedDetailUseCase {
    override fun getDogBreedById(id: Int): Flow<DogBreed> {
        return dataRepository.getDogBreedById(id)
    }

}