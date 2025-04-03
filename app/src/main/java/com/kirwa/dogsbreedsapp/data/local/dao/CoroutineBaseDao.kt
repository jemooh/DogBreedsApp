package com.kirwa.dogsbreedsapp.data.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
/**
 * Base DAO interface providing common coroutine-compatible operations:
 *
 * 1. Bulk insert ([insertAsync] with List<T>)
 * 2. Single item insert ([insertAsync])
 * 3. Item update ([updateAsync])
 * 4. Item deletion ([deleteAsync])
 *
 * Uses REPLACE conflict strategy for inserts/updates.
 * All operations are suspend functions for coroutine integration.
 */
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
