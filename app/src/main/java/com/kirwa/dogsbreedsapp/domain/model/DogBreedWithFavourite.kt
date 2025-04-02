package com.kirwa.dogsbreedsapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class DogBreedWithFavourite(
    @Embedded // Remove prefix since we're handling conflicts differently
    val dogBreed: DogBreed,

    @ColumnInfo(name = "is_favourite") // Match exactly with SQL alias
    val isFavourite: Boolean
)
