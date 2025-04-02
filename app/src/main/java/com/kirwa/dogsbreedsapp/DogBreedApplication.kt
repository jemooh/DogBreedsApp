package com.kirwa.dogsbreedsapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.kirwa.dogsbreedsapp.di.appModule
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.error.KoinAppAlreadyStartedException
import timber.log.Timber

class DogBreedApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initTimber()
        initKoin()
    }

    private fun initKoin() {
        try {
            startKoin {
                androidContext(this@DogBreedApplication)
                modules(
                    appModule
                )
            }
        } catch (error: KoinAppAlreadyStartedException) {
            Timber.e(error.localizedMessage)
        }
    }

    private fun initTimber() {
        Timber.plant(object : Timber.DebugTree() {
            @Nullable
            override fun createStackElementTag(@NotNull element: StackTraceElement): String? {
                return super.createStackElementTag(element) + ":" + element.lineNumber
            }
        })
    }
}
