package com.kirwa.dogsbreedsapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.kirwa.dogsbreedsapp.domain.model.DogBreed

class DogBreedDiffCallback : DiffUtil.ItemCallback<DogBreed>() {
    override fun areItemsTheSame(oldItem: DogBreed, newItem: DogBreed): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DogBreed, newItem: DogBreed): Boolean {
        return oldItem == newItem
    }
}
