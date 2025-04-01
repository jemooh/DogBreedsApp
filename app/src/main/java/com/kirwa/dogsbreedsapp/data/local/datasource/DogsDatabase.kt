package com.kirwa.dogsbreedsapp.data.local.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kirwa.dogsbreedsapp.data.local.dao.DogBreedsDao
import com.kirwa.dogsbreedsapp.data.local.dao.FavouriteDogBreedsDao
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed

@Database(
    entities = [DogBreed::class, FavouriteDogBreed::class],
    version = 2

)
@TypeConverters(Converters::class)
abstract class DogsDatabase : RoomDatabase() {
    abstract val favouriteDogBreedsDao: FavouriteDogBreedsDao
    abstract val dogBreedsDao: DogBreedsDao

    companion object {
        const val DATABASE_NAME = "dogs_db"
    }
}
