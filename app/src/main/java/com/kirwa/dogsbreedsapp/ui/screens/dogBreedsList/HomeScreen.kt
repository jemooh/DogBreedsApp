package com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.kirwa.dogsbreedsapp.R
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.ui.navigation.NavigationItem
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.viewmodel.DogBreedsListViewModel
import com.kirwa.dogsbreedsapp.utils.Constants
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navController: NavController) {
    val dogBreedsListViewModel: DogBreedsListViewModel = koinViewModel()
    dogBreedsListViewModel.fetchRemoteDogBreeds()
    dogBreedsListViewModel.getLocalDogBreeds()
    val uiState = dogBreedsListViewModel.state.collectAsState().value
    Column {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(uiState.dogs) { dog ->
                DogBreedItem(dog) {
                    navController.navigate(NavigationItem.DogBreedDetails.createRoute(dog.id))
                }
            }
        }
    }
}

@Composable
fun DogBreedItem(dog: DogBreed, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
            .background(Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            val imageUrl = Constants.IMAGES_BASE_URL + "${dog.referenceImageId}.jpg"

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = imageUrl,
                        placeholder = painterResource(id = R.drawable.baseline_image_search_24),
                        error = painterResource(id = R.drawable.baseline_image_24)
                    ),
                    contentDescription = dog.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(height = 60.dp, width = 80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                )

                if (dog.isFavourite) {
                    Text(
                        text = "Favourite",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .background(colorResource(id = R.color.orange), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(text = dog.name ?: "", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "Bred for: ${dog.bredFor ?: ""}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}



