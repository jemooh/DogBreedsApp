package com.kirwa.dogsbreedsapp.ui.screens.dogBreedDetails

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.kirwa.dogsbreedsapp.R
import com.kirwa.dogsbreedsapp.domain.model.FavouriteDogBreed
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedDetails.viewmodel.DogBreedDetailViewModel
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.viewmodel.DogBreedsListViewModel
import com.kirwa.dogsbreedsapp.utils.Constants
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DogDetailsScreen(dogId: String, navController: NavController) {
    val dogBreedsViewModel: DogBreedDetailViewModel = koinViewModel()
    val scrollState = rememberScrollState()

    LaunchedEffect(dogId) {
        dogBreedsViewModel.getDogBreedById(dogId.toInt())
    }

    val uiState = dogBreedsViewModel.state.collectAsState().value
    val dog = uiState.dog

    // Handle the favorite button state
    var isFavorite by remember { mutableStateOf(dog?.isFavourite ?: false) }

    // Update the isFavorite value if it changes
    LaunchedEffect(dog?.isFavourite) {
        isFavorite = dog?.isFavourite ?: false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imageUrl = Constants.IMAGES_BASE_URL + "${dog?.referenceImageId}.jpg"

        // Back Button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.popBackStack() }
                .padding(vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Details", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = rememberAsyncImagePainter(
                model = imageUrl,
                placeholder = painterResource(id = R.drawable.baseline_image_search_24),
                error = painterResource(id = R.drawable.baseline_image_24)
            ),
            contentDescription = dog?.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = dog?.name ?: "Unknown",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            DetailItem(label = "Weight", value = dog?.weight)
            DetailItem(label = "Height", value = dog?.height)
            DetailItem(label = "Bred For", value = dog?.bredFor)
            DetailItem(label = "Breed Group", value = dog?.breedGroup)
            DetailItem(label = "Temperament", value = dog?.temperament)
            DetailItem(label = "Origin", value = dog?.origin)
            DetailItem(label = "Life Span", value = dog?.lifeSpan)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isFavorite) {
                    dog?.let {
                        dogBreedsViewModel.deleteFavouriteDogBreed(it.id)
                    }
                } else {
                    dog?.let {
                        val favouriteDogBreed = FavouriteDogBreed(
                            id = it.id ?: 0,
                            name = it.name,
                            bredFor = it.bredFor,
                            height = it.height,
                            weight = it.weight,
                            lifeSpan = it.lifeSpan,
                            temperament = it.temperament,
                            referenceImageId = it.referenceImageId,
                            breedGroup = it.breedGroup,
                            origin = it.origin,
                            imageUrl = it.imageUrl
                        )
                        dogBreedsViewModel.saveFavouriteDogBreed(favouriteDogBreed)
                    }
                }
                isFavorite = !isFavorite
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFavorite) Color.Red else Color.Blue
            )
        ) {
            Text(text = if (isFavorite) "Remove from Favourite" else "Add to Favourite")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DetailItem(label: String, value: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(text = "$label: ", color = Color.Gray, fontWeight = FontWeight.Medium)
        Text(text = value ?: "N/A", color = Color.Black, fontWeight = FontWeight.Bold)
    }
}
