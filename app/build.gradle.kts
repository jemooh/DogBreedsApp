plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.kirwa.dogsbreedsapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kirwa.dogsbreedsapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Core Dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Retrofit & Gson
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Koin (Dependency Injection)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // Room (Local Database)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Kotlin Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Jetpack Compose Navigation
    implementation(libs.navigation.compose)

    // Image Loading (Coil)
    implementation(libs.coil.compose)

    implementation(libs.timber)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(libs.junit4)

    // JUnit5 & Spek
    testImplementation(libs.junit5)
    testRuntimeOnly(libs.junit5.vintage)

    // MockK for mocking
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.agent)

    // Koin Test
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit5)

    // Coroutines & Flow Testing
    testImplementation(libs.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.core.testing)


    // Room Database Test
    testImplementation(libs.room.testing)

    // Mock Web Server
    testImplementation(libs.mock.webserver)

    // Truth Assertions
    testImplementation(libs.truth)

    // Robolectric for Android unit testing
    testImplementation(libs.robolectric)

}