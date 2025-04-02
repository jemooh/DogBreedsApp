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
