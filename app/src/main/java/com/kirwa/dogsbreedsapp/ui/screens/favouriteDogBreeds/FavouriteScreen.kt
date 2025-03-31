package com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kirwa.dogsbreedsapp.R
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import com.kirwa.dogsbreedsapp.ui.navigation.NavigationItem
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.viewmodel.DogBreedsListViewModel
import com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.viewmodel.FavouriteDogBreedsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavouriteScreen(navController: NavController) {
    val dogBreedsListViewModel: FavouriteDogBreedsViewModel = koinViewModel()
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
            .padding(32.dp),
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
        Text(text = message, style = MaterialTheme.typography.headlineMedium, color = Color.Gray)
    }
}

@Composable
fun DogBreedItem(dog: FavouriteDogBreed, onClick: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { onClick() }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = dog.name ?: "", style = MaterialTheme.typography.headlineMedium)
            Text(text = dog.bredFor ?: "", style = MaterialTheme.typography.bodySmall)
        }
    }
}
