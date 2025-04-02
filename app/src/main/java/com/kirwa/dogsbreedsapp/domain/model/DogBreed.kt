package com.kirwa.dogsbreedsapp.domain.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["id"], unique = true)])
data class DogBreed(
    @PrimaryKey
    @NonNull
    val id: Int = 0,
    val name: String?,
    val weight: String?,
    val height: String?,
    val bredFor: String?,
    val breedGroup: String?,
    val temperament: String?,
    val origin: String?,
    val lifeSpan: String?,
    val referenceImageId: String?,
    val imageUrl: String?,
    val isFavourite: Boolean = false
)
