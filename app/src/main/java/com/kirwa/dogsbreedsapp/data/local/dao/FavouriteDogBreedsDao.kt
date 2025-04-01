package com.kirwa.dogsbreedsapp.data.local.dao

import androidx.room.*
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDogBreedsDao : CoroutineBaseDao<FavouriteDogBreed> {
    @Query("SELECT * FROM FavouriteDogBreed order by name ASC Limit 100 ")
    fun getFavouriteDogBreeds(): Flow<List<FavouriteDogBreed>>

    @Query("DELETE FROM FavouriteDogBreed WHERE id = :id")
    suspend fun deleteFavouriteDogBreedById(id: Int)

}
