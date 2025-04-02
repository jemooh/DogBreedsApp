package com.kirwa.dogsbreedsapp.data.remote.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.kirwa.dogsbreedsapp.data.local.dao.DogBreedsDao
import com.kirwa.dogsbreedsapp.data.remote.apiService.DogsApiService
import com.kirwa.dogsbreedsapp.data.remote.model.DogBreedsResponse
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.utils.ConnectivityHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import android.util.Log
import com.kirwa.dogsbreedsapp.data.local.dao.RemoteKeyDao
import com.kirwa.dogsbreedsapp.domain.model.DogBreedWithFavourite
import com.kirwa.dogsbreedsapp.domain.model.RemoteKey

@OptIn(ExperimentalPagingApi::class)
class DogBreedsRemoteMediator(
    private val apiService: DogsApiService,
    private val dogBreedsDao: DogBreedsDao,
    private val remoteKeyDao: RemoteKeyDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteMediator<Int, DogBreedWithFavourite>() { // Changed to DogBreedWithFavourite

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DogBreedWithFavourite>
    ): MediatorResult {
        return withContext(ioDispatcher) {
            try {
                val page = when (loadType) {
                    LoadType.REFRESH -> 1
                    LoadType.PREPEND -> return@withContext MediatorResult.Success(endOfPaginationReached = true)
                    LoadType.APPEND -> getLastRemoteKey()?.nextKey
                        ?: return@withContext MediatorResult.Success(endOfPaginationReached = true)
                }

                val response = apiService.fetchDogBreeds(
                    limit = state.config.pageSize,
                    page = page
                )

                if (!response.isSuccessful) {
                    return@withContext MediatorResult.Error(Exception("API Error"))
                }

                val body = response.body() ?: return@withContext MediatorResult.Success(
                    endOfPaginationReached = true
                )

                // Convert API response to DogBreedWithFavourite
                val dogBreedsWithFav = body.mapNotNull { responseItem ->
                    responseItem.toDogBreed()?.let { dogBreed ->
                        DogBreedWithFavourite(
                            dogBreed = dogBreed,
                            isFavourite = false // Default to false, DB will update this later
                        )
                    }
                }

                val endOfPaginationReached = body.size < state.config.pageSize

                withContext(ioDispatcher) {
                    // Insert or update breeds (now handles DogBreedWithFavourite)
                    dogBreedsDao.insertAsync(dogBreedsWithFav.map { it.dogBreed })

                    // Update remote keys
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val prevKey = if (page == 1) null else page - 1

                    remoteKeyDao.insertAsync(
                        dogBreedsWithFav.map {
                            RemoteKey(
                                id = it.dogBreed.id,
                                prevKey = prevKey,
                                nextKey = nextKey
                            )
                        }
                    )
                }

                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } catch (e: Exception) {
                MediatorResult.Error(e)
            }
        }
    }

    private suspend fun getLastRemoteKey(): RemoteKey? {
        return remoteKeyDao.getLastKey()
    }

    private fun DogBreedsResponse.toDogBreed(): DogBreed? {
        return id?.let { nonNullId ->
            DogBreed(
                id = nonNullId,
                name = name ?: "Unknown Breed",
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
    }
}