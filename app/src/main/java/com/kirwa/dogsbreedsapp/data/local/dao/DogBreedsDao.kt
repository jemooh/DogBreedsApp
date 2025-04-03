package com.kirwa.dogsbreedsapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.DogBreedWithFavourite
import kotlinx.coroutines.flow.Flow
/**
 * Data Access Object for dog breed operations providing:
 *
 * 1. Paginated breed data with favorite status ([getPagedDogBreeds])
 *   - Combines [DogBreed] with [FavouriteDogBreed] via SQL JOIN
 *   - Returns [PagingSource] for efficient pagination
 *   - Orders results alphabetically by name
 *
 * 2. Individual breed lookup with favorite status ([getDogBreedById])
 *   - Uses LEFT JOIN to determine favorite status
 *   - Returns [Flow] for reactive updates
 *
 * Inherits common CRUD operations from [CoroutineBaseDao]
 */
@Dao
interface DogBreedsDao : CoroutineBaseDao<DogBreed> {
    @Query("""
    SELECT db.*, 
        CASE WHEN EXISTS (
            SELECT 1 FROM FavouriteDogBreed fd 
            WHERE fd.id = db.id
        ) THEN 1 ELSE 0 END AS is_favourite  
    FROM DogBreed db
    ORDER BY db.name ASC
""")
    fun getPagedDogBreeds(): PagingSource<Int, DogBreedWithFavourite>

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
