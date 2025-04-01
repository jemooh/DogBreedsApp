package com.kirwa.dogsbreedsapp.utils

import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import java.util.*


val favouriteDogBreeds = (0..2).map {
    val breed = FavouriteDogBreed(
        id = 5,
        name = "Akbash Dog",
        weight = "600",
        height = "471",
        bredFor = "Guarding",
        breedGroup = "Working",
        temperament = "Loyal, Independent, Intelligent, Brave",
        origin = "",
        lifeSpan = "lifeSpan",
        referenceImageId = "26pHT3Qk7",
        imageUrl = ""
    )
}
