package com.kirwa.dogsbreedsapp.data.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface CoroutineBaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsync(items: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsync(item: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAsync(item: T)

    @Delete
    suspend fun deleteAsync(item: T)
}
