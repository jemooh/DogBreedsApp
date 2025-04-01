package com.kirwa.dogsbreedsapp.domain.usecase.dogBreedsList

import com.kirwa.dogsbreedsapp.data.remote.model.Result
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import kotlinx.coroutines.flow.Flow

class DogBreedsListUseCaseImpl(
    private val dataRepository: DogBreedsRepository
) : DogBreedsListUseCase {
    override suspend fun fetchRemoteDogBreeds(): Result<Boolean> {
        return dataRepository.fetchRemoteDogBreeds()
    }

    override fun getLocalDogBreeds(): Flow<List<DogBreed>> {
        return dataRepository.getLocalDogBreeds()
    }

}