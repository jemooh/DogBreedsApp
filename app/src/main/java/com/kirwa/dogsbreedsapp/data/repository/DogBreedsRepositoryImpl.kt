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
