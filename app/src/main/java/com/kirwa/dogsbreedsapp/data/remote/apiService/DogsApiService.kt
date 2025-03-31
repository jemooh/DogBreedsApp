package com.kirwa.dogsbreedsapp.data.remote.apiService

import com.kirwa.dogsbreedsapp.data.remote.model.DogBreedsResponse
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import retrofit2.Response
import retrofit2.http.*

interface DogsApiService {
    @GET("breeds")
    suspend fun fetchDogBreeds(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): Response<DogBreedsResponse>

}
