package com.kirwa.dogsbreedsapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class DogBreedsResponse(

    @field:SerializedName("weight")
    val weight: Weight? = null,

    @field:SerializedName("height")
    val height: Height? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("bred_for")
    val bredFor: String? = null,

    @field:SerializedName("breed_group")
    val breedGroup: String? = null,

    @field:SerializedName("life_span")
    val lifeSpan: String? = null,

    @field:SerializedName("temperament")
    val temperament: String? = null,

    @field:SerializedName("origin")
    val origin: String? = null,

    @field:SerializedName("reference_image_id")
    val referenceImageId: String? = null,

    @field:SerializedName("image")
    val image: Image? = null
)

data class Weight(
    @field:SerializedName("imperial")
    val imperial: String? = null,

    @field:SerializedName("metric")
    val metric: String? = null
)

data class Height(
    @field:SerializedName("imperial")
    val imperial: String? = null,

    @field:SerializedName("metric")
    val metric: String? = null
)

data class Image(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("width")
    val width: Int? = null,

    @field:SerializedName("height")
    val height: Int? = null,

    @field:SerializedName("url")
    val url: String? = null
)