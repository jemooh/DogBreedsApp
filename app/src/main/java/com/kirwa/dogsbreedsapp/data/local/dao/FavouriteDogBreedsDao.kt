package com.kirwa.dogsbreedsapp.data.local.dao

import androidx.room.*
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import kotlinx.coroutines.flow.Flow
/**
 * Data Access Object for favorite dog breeds operations providing:
 *
 * 1. Favorite breeds listing ([getFavouriteDogBreeds])
 *   - Returns top 100 favorites ordered by name
 *   - Uses [Flow] for reactive updates
 *
 * 2. Favorite deletion ([deleteFavouriteDogBreedById])
 *   - Removes single favorite by ID
 *
 * Inherits common CRUD operations from [CoroutineBaseDao]
 */
@Dao
interface FavouriteDogBreedsDao : CoroutineBaseDao<FavouriteDogBreed> {
    @Query("SELECT * FROM FavouriteDogBreed order by name ASC Limit 100 ")
    fun getFavouriteDogBreeds(): Flow<List<FavouriteDogBreed>>

    @Query("DELETE FROM FavouriteDogBreed WHERE id = :id")
    suspend fun deleteFavouriteDogBreedById(id: Int)

}
