package com.kirwa.dogsbreedsapp.data.local.dao

import androidx.room.*
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import kotlinx.coroutines.flow.Flow

@Dao
interface DogBreedsDao : CoroutineBaseDao<DogBreed> {
    @Query("""
    SELECT db.*, 
           CASE 
               WHEN fd.id IS NOT NULL THEN 1 
               ELSE 0 
           END AS isFavourite
    FROM DogBreed db 
    LEFT JOIN FavouriteDogBreed fd 
    ON db.id = fd.id 
    ORDER BY name ASC 
    LIMIT 100
""")
    fun getDogBreeds(): Flow<List<DogBreed>>


    @Query("""
    SELECT db.*, 
           CASE 
               WHEN fd.id IS NOT NULL THEN 1 
               ELSE 0 
           END AS isFavourite
    FROM DogBreed db 
    LEFT JOIN FavouriteDogBreed fd 
    ON db.id = fd.id 
    WHERE db.id = :id
""")
    fun getDogBreedById(id: Int): Flow<DogBreed>

}
