package com.kirwa.dogsbreedsapp.ui.screens.dogBreedDetails

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DogDetailsScreen(dogId: String, navController: NavController) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Dog Details") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val dogImage = rememberImagePainter("https://example.com/image$dogId.jpg")
            Image(painter = dogImage, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(200.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Dog ID: $dogId", style = MaterialTheme.typography.headlineMedium)
        }
    }
}