package com.kirwa.dogsbreedsapp.di


import android.content.Context
import androidx.room.Room
import com.kirwa.dogsbreedsapp.data.local.datasource.DogsDatabase
import com.kirwa.dogsbreedsapp.data.local.datasource.SharedPreferences
import com.kirwa.dogsbreedsapp.data.remote.apiService.DogsApiService
import com.kirwa.dogsbreedsapp.data.repository.DogBreedsRepositoryImpl
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedDetails.viewmodel.DogBreedDetailViewModel
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.viewmodel.DogBreedsListViewModel
import com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.viewmodel.FavouriteDogBreedsViewModel
import com.kirwa.dogsbreedsapp.utils.Constants
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

const val baseUrl: String = Constants.BASE_URL
const val apiKey: String = Constants.APIKEY

val appModule = module {
    single { createNetworkClient(baseUrl, apiKey) }
    single { (get() as? Retrofit)?.create(DogsApiService::class.java) }

    single {
        Room.databaseBuilder(
            androidContext(),
            DogsDatabase::class.java,
            DogsDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    single {
        get<DogsDatabase>().dogBreedsDao
    }

    single {
        get<DogsDatabase>().favouriteDogBreedsDao
    }

    single<DogBreedsRepository> {
        DogBreedsRepositoryImpl(
            dogsApiService = get(),
            dogBreedsDao = get(),
            favouriteDogBreedsDao = get()
        )
    }

    viewModel {
        DogBreedsListViewModel(dogBreedsRepository = get())
    }

    viewModel {
        DogBreedDetailViewModel(dogBreedsRepository = get())
    }

    viewModel {
        FavouriteDogBreedsViewModel(dogBreedsRepository = get())
    }

    single {
        androidApplication().getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    }
    single {
        SharedPreferences(get())
    }
}
