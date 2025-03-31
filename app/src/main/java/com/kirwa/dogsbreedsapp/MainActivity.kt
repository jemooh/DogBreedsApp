package com.kirwa.dogsbreedsapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kirwa.dogsbreedsapp.ui.screens.MainScreen
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.viewmodel.DogBreedsListViewModel
import com.kirwa.dogsbreedsapp.ui.theme.DogsBreedsAppTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogsBreedsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val dogBreedsListViewModel: DogBreedsListViewModel = koinViewModel()
    dogBreedsListViewModel.fetchRemoteDogBreeds()
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DogsBreedsAppTheme {
        Greeting("Android")
    }
}*/
