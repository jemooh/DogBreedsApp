package com.kirwa.dogsbreedsapp.domain.usecase.dogBreedDetails

import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import kotlinx.coroutines.flow.Flow

class DogBreedDetailUseCaseImpl(
    private val dataRepository: DogBreedsRepository
) : DogBreedDetailUseCase {
    override fun getDogBreedById(id: Int): Flow<DogBreed> {
        return dataRepository.getDogBreedById(id)
    }

}