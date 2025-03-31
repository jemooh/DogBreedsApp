package com.kirwa.dogsbreedsapp.data.local.dao

import androidx.room.*
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import kotlinx.coroutines.flow.Flow

@Dao
interface DogBreedsDao : CoroutineBaseDao<DogBreed> {
    @Query("SELECT * FROM DogBreed order by name ASC Limit 100 ")
    fun getDogBreeds(): Flow<List<DogBreed>>

    @Query("SELECT * FROM DogBreed WHERE id = :id ")
    fun getDogBreedById(id: Int): Flow<DogBreed>

    @Query("SELECT * FROM DogBreed WHERE name  LIKE :searchString ")
    fun searchDogBreeds(searchString: String?): Flow<List<DogBreed>>

}
