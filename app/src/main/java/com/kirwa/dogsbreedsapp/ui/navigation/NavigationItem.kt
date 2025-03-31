package com.kirwa.dogsbreedsapp.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.kirwa.dogsbreedsapp.R
import java.util.ResourceBundle

sealed class NavigationItem(
    val route: String,
    @StringRes val title: Int?,
    val icon: ImageVector?
) {

    object Home : NavigationItem("home", R.string.text_home, Icons.Rounded.Home)
    object Favourites :
        NavigationItem("favourites", R.string.text_favourites, Icons.Rounded.Favorite)
    object DogBreedDetails : NavigationItem("details/{dogId}", R.string.text_details, null) {
        fun createRoute(dogId: Int) = "details/$dogId"
    }
}
