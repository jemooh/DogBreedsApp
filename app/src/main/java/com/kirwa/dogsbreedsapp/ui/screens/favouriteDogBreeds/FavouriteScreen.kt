package com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.kirwa.dogsbreedsapp.R
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import com.kirwa.dogsbreedsapp.ui.navigation.NavigationItem
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.viewmodel.DogBreedsListViewModel
import com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.viewmodel.FavouriteDogBreedsViewModel
import com.kirwa.dogsbreedsapp.utils.Constants
import org.koin.androidx.compose.koinViewModel
/**
 * FavouriteScreen displays a list of favourite dog breeds stored locally.
 * If there are no favourite breeds, it shows an empty state screen.
 *
 * - Uses Koin for dependency injection to retrieve the ViewModel.
 * - Observes state changes using collectAsState.
 * - Displays a list of favourite dog breeds using LazyColumn.
 * - Navigates to the dog breed details screen on item click.
 */
@Composable
fun FavouriteScreen(navController: NavController) {
    val dogBreedsListViewModel: FavouriteDogBreedsViewModel = koinViewModel()
    dogBreedsListViewModel.getLocalFavouriteDogBreeds()
    val uiState = dogBreedsListViewModel.state.collectAsState().value

    if (uiState.favouriteDogBreeds.isEmpty()) {
        EmptyStateScreen(
            message = stringResource(R.string.text_no_favourites),
            icon = Icons.Rounded.FavoriteBorder
        )
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(uiState.favouriteDogBreeds) { dog ->
                DogBreedItem(dog) {
                    navController.navigate(NavigationItem.DogBreedDetails.createRoute(dog.id))
                }
            }
        }
    }
}

@Composable
fun EmptyStateScreen(message: String, icon: ImageVector) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Empty state icon",
            tint = Color.Gray,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = message, style = MaterialTheme.typography.titleMedium, color = Color.Gray)
    }
}

@Composable
fun DogBreedItem(dog: FavouriteDogBreed, onClick: () -> Unit) {
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