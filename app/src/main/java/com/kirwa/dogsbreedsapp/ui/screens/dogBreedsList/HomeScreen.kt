package com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList


import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
/**
 * Composable functions for displaying the Home Screen and Dog Breed Items.
 *
 * - `HomeScreen`: Fetches and displays a list of dog breeds. Clicking an item navigates to the details screen.
 * - `DogBreedItem`: Displays an individual dog breed with an image, name, and favorite tag.
 *
 * Uses:
 * - Jetpack Compose for UI.
 * - Koin for dependency injection.
 * - Coil for image loading.
 */
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val isConnected = connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true

    val dogBreedsListViewModel: DogBreedsListViewModel = koinViewModel()

    // Fetch local data first
    dogBreedsListViewModel.getLocalDogBreeds()

    // Fetch remote data only if internet is available
    if (isConnected) {
        dogBreedsListViewModel.fetchRemoteDogBreeds()
    }

    val uiState = dogBreedsListViewModel.state.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            uiState.isRefreshing -> {
                // Show Loading Progress Indicator while fetching data
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp)
                )
            }

            !isConnected && uiState.dogs.isNotEmpty() -> {
                // Small "Offline Mode" banner (Yellow) with Warning Icon
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Yellow)
                        .padding(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Offline Mode",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Offline Mode: Showing saved records",
                            color = Color.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            !isConnected && uiState.dogs.isEmpty() -> {
                // Large "No Internet & No Data" banner (Red) with No Connection Icon
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Red)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_wifi_off_24),
                            contentDescription = "No Internet",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "No internet connection. No saved records available.",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Show the list only when data is available
        if (!uiState.isRefreshing && uiState.dogs.isNotEmpty()) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(uiState.dogs) { dog ->
                    DogBreedItem(dog) {
                        navController.navigate(NavigationItem.DogBreedDetails.createRoute(dog.id))
                    }
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



