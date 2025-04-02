package com.kirwa.dogsbreedsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import com.kirwa.dogsbreedsapp.domain.model.RemoteKey

@Dao
interface RemoteKeyDao :  CoroutineBaseDao<RemoteKey>{
    @Query("SELECT * FROM RemoteKey WHERE id = :id")
    suspend fun getByDogBreedId(id: Int): RemoteKey?
    @Query("SELECT * FROM RemoteKey ORDER BY id DESC LIMIT 1")
    suspend fun getLastKey(): RemoteKey?
}