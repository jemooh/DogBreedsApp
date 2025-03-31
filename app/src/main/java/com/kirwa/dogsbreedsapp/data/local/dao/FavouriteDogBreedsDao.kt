package com.kirwa.dogsbreedsapp.data.local.dao

import androidx.room.*
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDogBreedsDao : CoroutineBaseDao<FavouriteDogBreed> {
    @Query("SELECT * FROM FavouriteDogBreed order by name ASC Limit 100 ")
    fun getFavouriteDogBreeds(): Flow<List<FavouriteDogBreed>>

    @Query("SELECT * FROM FavouriteDogBreed WHERE name  LIKE :searchString ")
    fun searchFavouriteDogBreeds(searchString: String?): Flow<List<FavouriteDogBreed>>
}
