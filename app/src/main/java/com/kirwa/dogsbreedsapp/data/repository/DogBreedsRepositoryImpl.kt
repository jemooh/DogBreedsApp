package com.kirwa.dogsbreedsapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kirwa.dogsbreedsapp.data.local.dao.DogBreedsDao
import com.kirwa.dogsbreedsapp.data.local.dao.FavouriteDogBreedsDao
import com.kirwa.dogsbreedsapp.data.local.dao.RemoteKeyDao
import com.kirwa.dogsbreedsapp.data.remote.apiService.DogsApiService
import com.kirwa.dogsbreedsapp.data.remote.datasource.DogBreedsRemoteMediator
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import java.io.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import com.kirwa.dogsbreedsapp.data.remote.model.Result
import com.kirwa.dogsbreedsapp.domain.model.DogBreedWithFavourite
import com.kirwa.dogsbreedsapp.utils.ConnectivityHelper
import timber.log.Timber

/**
 * Implementation of [DogBreedsRepository] that handles:
 *
 * 1. Paginated dog breeds data with remote and local caching
 * 2. Favorite dog breeds management
 * 3. Breed detail fetching
 *
 * Uses [DogBreedsRemoteMediator] for coordinated network/database paging
 * and maintains data consistency across multiple data sources:
 * - [DogsApiService] for remote data
 * - [DogBreedsDao] for local cache
 * - [FavouriteDogBreedsDao] for favorites storage
 * - [RemoteKeyDao] for pagination metadata
 */
class DogBreedsRepositoryImpl(
    private val apiService: DogsApiService,
    private val dogBreedsDao: DogBreedsDao,
    private val favouriteDogBreedsDao: FavouriteDogBreedsDao,
    private val remoteKeyDao: RemoteKeyDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DogBreedsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedDogBreeds(): Flow<PagingData<DogBreedWithFavourite>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            remoteMediator = DogBreedsRemoteMediator(
                apiService = apiService,
                dogBreedsDao = dogBreedsDao,
                remoteKeyDao = remoteKeyDao,
                ioDispatcher = ioDispatcher
            ),
            pagingSourceFactory = { dogBreedsDao.getPagedDogBreeds() }
        ).flow
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
