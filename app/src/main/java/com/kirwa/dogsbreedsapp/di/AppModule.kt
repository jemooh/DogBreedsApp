package com.kirwa.dogsbreedsapp.di


import android.content.Context
import androidx.room.Room
import com.kirwa.dogsbreedsapp.data.local.datasource.DogsDatabase
import com.kirwa.dogsbreedsapp.data.local.datasource.SharedPreferences
import com.kirwa.dogsbreedsapp.data.remote.apiService.DogsApiService
import com.kirwa.dogsbreedsapp.data.repository.DogBreedsRepositoryImpl
import com.kirwa.dogsbreedsapp.domain.repository.DogBreedsRepository
import com.kirwa.dogsbreedsapp.domain.usecase.dogBreedDetails.DogBreedDetailUseCase
import com.kirwa.dogsbreedsapp.domain.usecase.dogBreedDetails.DogBreedDetailUseCaseImpl
import com.kirwa.dogsbreedsapp.domain.usecase.dogBreedsList.DogBreedsListUseCase
import com.kirwa.dogsbreedsapp.domain.usecase.dogBreedsList.DogBreedsListUseCaseImpl
import com.kirwa.dogsbreedsapp.domain.usecase.favouriteDogBreeds.FavouriteDogBreedsUseCase
import com.kirwa.dogsbreedsapp.domain.usecase.favouriteDogBreeds.FavouriteDogBreedsUseCaseImpl
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedDetails.viewmodel.DogBreedDetailViewModel
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.viewmodel.DogBreedsListViewModel
import com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.viewmodel.FavouriteDogBreedsViewModel
import com.kirwa.dogsbreedsapp.utils.ConnectivityHelper
import com.kirwa.dogsbreedsapp.utils.Constants
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
/**
 * Koin dependency injection module providing:
 *
 * 1. Network components:
 *    - Retrofit API service
 *    - Network client configuration
 *
 * 2. Database components:
 *    - Room database instance
 *    - DAO accessors
 *
 * 3. Application components:
 *    - Repository implementations
 *    - Use cases
 *    - ViewModels
 *    - SharedPreferences
 *
 * Configures all application dependencies with proper scoping.
 */
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

    single {
        get<DogsDatabase>().remoteKeyDao
    }

    single { ConnectivityHelper(get()) }

    single<DogBreedsRepository> {
        DogBreedsRepositoryImpl(
            apiService = get(),
            dogBreedsDao = get(),
            remoteKeyDao = get(),
            favouriteDogBreedsDao = get()
        )
    }

    viewModel {
        DogBreedsListViewModel(useCase = get(), connectivityHelper = get())
    }

    viewModel {
        DogBreedDetailViewModel(dogBreedDetailUseCase = get())
    }

    viewModel {
        FavouriteDogBreedsViewModel(favouriteDogBreedsUseCase = get())
    }

    single<DogBreedsListUseCase> { DogBreedsListUseCaseImpl(get()) }
    single<DogBreedDetailUseCase> { DogBreedDetailUseCaseImpl(get()) }
    single<FavouriteDogBreedsUseCase> { FavouriteDogBreedsUseCaseImpl(get()) }

    single {
        androidApplication().getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    }
    single {
        SharedPreferences(get())
    }
}
