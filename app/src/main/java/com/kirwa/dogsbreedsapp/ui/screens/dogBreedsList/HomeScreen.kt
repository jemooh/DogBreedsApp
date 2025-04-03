package com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.viewmodel.DogBreedsListViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.paging.compose.collectAsLazyPagingItems


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.kirwa.dogsbreedsapp.R
import com.kirwa.dogsbreedsapp.domain.model.DogBreed
import com.kirwa.dogsbreedsapp.domain.model.DogBreedWithFavourite
import com.kirwa.dogsbreedsapp.ui.navigation.NavigationItem
import com.kirwa.dogsbreedsapp.utils.Constants

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
 * -  Lazy-loaded items with navigation support
 */
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: DogBreedsListViewModel = koinViewModel()
) {
    val lazyPagingItems = viewModel.dogBreeds.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Handle error states
        when {
            !uiState.isNetworkAvailable && lazyPagingItems.itemCount > 0 -> {
                OfflineBanner()
            }
            !uiState.isNetworkAvailable && lazyPagingItems.itemCount == 0 -> {
                NoConnectionError(onRetry = { viewModel.retry() })
            }
            lazyPagingItems.loadState.refresh is LoadState.Error -> {
                val error = (lazyPagingItems.loadState.refresh as LoadState.Error).error
                ErrorBanner(
                    message = error.message ?: "Error loading data",
                    onRetry = { lazyPagingItems.retry() }
                )
            }
        }

        // Main content
        when {
            uiState.isLoading && lazyPagingItems.itemCount == 0 -> {
                FullScreenLoading()
            }
            else -> {
                DogBreedsList(
                    lazyPagingItems = lazyPagingItems,
                    navController = navController,
                    onRetry = { lazyPagingItems.retry() }
                )
            }
        }
    }
}

@Composable
fun ErrorBanner(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            TextButton(
                onClick = onRetry,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("Retry")
            }
        }
    }
}

@Composable
private fun NoConnectionError(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.error)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_wifi_off_24), // Use your drawable here
            contentDescription = "No connection",
            tint = MaterialTheme.colorScheme.onError,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "No internet connection",
            color = MaterialTheme.colorScheme.onError,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onError,
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Retry")
        }
    }
}

@Composable
private fun DogBreedsList(
    lazyPagingItems: LazyPagingItems<DogBreedWithFavourite>,
    navController: NavController,
    onRetry: () -> Unit
) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(lazyPagingItems.itemCount) { index ->
            val dog = lazyPagingItems[index]
            dog?.let {
                DogBreedItem(dog) {
                    navController.navigate(NavigationItem.DogBreedDetails.createRoute(dog.dogBreed.id))
                }
            }
        }

        when (lazyPagingItems.loadState.append) {
            is LoadState.Loading -> {
                item { LoadingIndicator() }
            }
            is LoadState.Error -> {
                item {
                    ErrorItem(
                        message = "Error loading more items",
                        onRetry = onRetry
                    )
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun ErrorItem(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text("Retry")
        }
    }
}

@Composable
private fun OfflineBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Yellow)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Warning, "Offline", tint = Color.Black)
            Spacer(Modifier.width(8.dp))
            Text("Offline Mode: Showing saved records", color = Color.Black)
        }
    }
}


@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun LoadingIndicator() {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("No dog breeds found", style = MaterialTheme.typography.labelMedium)
    }
}




@Composable
fun DogBreedItem(dog: DogBreedWithFavourite, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
            .background(Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            val imageUrl = Constants.IMAGES_BASE_URL + "${dog.dogBreed.referenceImageId}.jpg"

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = imageUrl,
                        placeholder = painterResource(id = R.drawable.baseline_image_search_24),
                        error = painterResource(id = R.drawable.baseline_image_24)
                    ),
                    contentDescription = dog.dogBreed.name,
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
                Text(text = dog.dogBreed.name ?: "", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "Bred for: ${dog.dogBreed.bredFor ?: ""}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}



