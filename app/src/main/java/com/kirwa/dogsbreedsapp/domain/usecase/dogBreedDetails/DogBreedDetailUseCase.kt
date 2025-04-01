package com.kirwa.dogsbreedsapp.domain.usecase.dogBreedDetails

import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import kotlinx.coroutines.flow.Flow


interface DogBreedDetailUseCase {
    fun getDogBreedById(id: Int): Flow<DogBreed>
}