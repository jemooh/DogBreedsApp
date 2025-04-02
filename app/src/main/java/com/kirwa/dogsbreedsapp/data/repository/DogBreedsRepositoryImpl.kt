package com.kirwa.dogsbreedsapp.data.repository

import com.kirwa.dogsbreedsapp.data.local.dao.DogBreedsDao
import com.kirwa.dogsbreedsapp.data.local.dao.FavouriteDogBreedsDao
import com.kirwa.dogsbreedsapp.data.remote.apiService.DogsApiService
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import java.io.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import com.kirwa.dogsbreedsapp.data.remote.model.Result
import timber.log.Timber

internal class DogBreedsRepositoryImpl(
    private val dogsApiService: DogsApiService,
    private val dogBreedsDao: DogBreedsDao,
    private val favouriteDogBreedsDao: FavouriteDogBreedsDao,
    private val isDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DogBreedsRepository {
    override suspend fun fetchRemoteDogBreeds(): Result<Boolean> {
        return withContext(isDispatcher) {
            try {
                Result.Loading
                val limit = 50
                val page = 1
                val result = dogsApiService.fetchDogBreeds(limit, page)

                if (result.isSuccessful) {
                    result.body()?.forEach { remoteDogBreed ->
                        remoteDogBreed.apply {
                            val dogBreed = id?.let { id ->
                                DogBreed(
                                    id = id,
                                    name,
                                    weight = weight?.metric,
                                    height = height?.metric,
                                    bredFor = bredFor,
                                    breedGroup = breedGroup,
                                    temperament = temperament,
                                    origin = origin,
                                    lifeSpan = lifeSpan,
                                    referenceImageId = referenceImageId,
                                    imageUrl = image?.url
                                )
                            }
                            dogBreed?.let { dogBreedsDao.insertAsync(it) }
                        }
                    }
                    return@withContext Result.Success(true)
                } else {
                    return@withContext Result.Success(false)
                }
            } catch (e: IOException) {
                return@withContext Result.Error(Exception("Error Occurred"))
            }
        }
    }


    override fun getLocalDogBreeds(): Flow<List<DogBreed>> {
        return dogBreedsDao.getDogBreeds()
    }

    override fun getDogBreedById(id: Int): Flow<DogBreed> {
        return dogBreedsDao.getDogBreedById(id)
    }

    override suspend fun deleteFavouriteDogBreed(dogId: Int) {
        favouriteDogBreedsDao.deleteFavouriteDogBreedById(dogId)
    }

    override fun getLocalFavouriteDogBreeds(): Flow<List<FavouriteDogBreed>> {
        return favouriteDogBreedsDao.getFavouriteDogBreeds()
    }

    override suspend fun saveFavouriteDogBreed(dog: FavouriteDogBreed) {
        favouriteDogBreedsDao.insertAsync(dog)
    }

}
