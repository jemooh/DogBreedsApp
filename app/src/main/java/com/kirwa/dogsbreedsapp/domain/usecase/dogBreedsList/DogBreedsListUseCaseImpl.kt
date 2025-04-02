package com.kirwa.dogsbreedsapp.domain.usecase.dogBreedsList

import androidx.paging.PagingData
import com.kirwa.dogsbreedsapp.data.remote.model.Result
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.DogBreedWithFavourite
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import kotlinx.coroutines.flow.Flow

class DogBreedsListUseCaseImpl(
    private val dataRepository: DogBreedsRepository
) : DogBreedsListUseCase {
    override fun getPagedDogBreeds(): Flow<PagingData<DogBreedWithFavourite>> {
        return dataRepository.getPagedDogBreeds()
    }
}