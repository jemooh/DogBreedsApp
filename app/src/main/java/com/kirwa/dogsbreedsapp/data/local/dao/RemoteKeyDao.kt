package com.kirwa.dogsbreedsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import com.kirwa.dogsbreedsapp.domain.model.RemoteKey
/**
 * Data Access Object for pagination metadata operations providing:
 *
 * 1. Key lookup by breed ID ([getByDogBreedId])
 * 2. Last key retrieval ([getLastKey])
 *
 * Manages [RemoteKey] entities used by [DogBreedsRemoteMediator]
 * for maintaining pagination state across sessions.
 *
 * Inherits common CRUD operations from [CoroutineBaseDao]
 */
@Dao
interface RemoteKeyDao :  CoroutineBaseDao<RemoteKey>{
    @Query("SELECT * FROM RemoteKey WHERE id = :id")
    suspend fun getByDogBreedId(id: Int): RemoteKey?
    @Query("SELECT * FROM RemoteKey ORDER BY id DESC LIMIT 1")
    suspend fun getLastKey(): RemoteKey?
}