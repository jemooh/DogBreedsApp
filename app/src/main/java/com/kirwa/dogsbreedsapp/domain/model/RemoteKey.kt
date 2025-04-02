package com.kirwa.dogsbreedsapp.domain.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["id"], unique = true)])
data class RemoteKey(
    @PrimaryKey
    @NonNull
    val id: Int = 0,
    val prevKey: Int?,
    val nextKey: Int?
)
