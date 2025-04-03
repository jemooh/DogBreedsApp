package com.kirwa.dogsbreedsapp.data.local.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kirwa.dogsbreedsapp.data.local.dao.DogBreedsDao
import com.kirwa.dogsbreedsapp.data.local.dao.FavouriteDogBreedsDao
import com.kirwa.dogsbreedsapp.data.local.dao.RemoteKeyDao
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import com.kirwa.dogsbreedsapp.domain.model.RemoteKey
/**
 * Room Database class representing the local persistence layer for:
 *
 * 1. Dog breed information ([DogBreed] entities)
 * 2. User favorites ([FavouriteDogBreed] entities)
 * 3. Pagination metadata ([RemoteKey] entities)
 *
 * Provides DAO access through:
 * - [favouriteDogBreedsDao] - For favorite breed operations
 * - [dogBreedsDao] - For general breed data access
 * - [remoteKeyDao] - For pagination state management
 *
 * Configuration:
 * - Database version: 7 (incremented on schema changes)
 * - Type converters: [Converters] for custom type serialization
 * - Name: "dogs_db" (see [DATABASE_NAME])
 *
 * Implements destructive migration strategy for simplicity during development.
 */
@Database(
    entities = [DogBreed::class, FavouriteDogBreed::class, RemoteKey::class],
    version = 7

)
@TypeConverters(Converters::class)
abstract class DogsDatabase : RoomDatabase() {
    abstract val favouriteDogBreedsDao: FavouriteDogBreedsDao
    abstract val dogBreedsDao: DogBreedsDao
    abstract val remoteKeyDao: RemoteKeyDao

    companion object {
        const val DATABASE_NAME = "dogs_db"
    }
}
