package com.kirwa.dogsbreedsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedDetails.DogDetailsScreen
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.HomeScreen
import com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.FavouriteScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) { HomeScreen(navController) }
        composable(NavigationItem.Favourites.route) { FavouriteScreen(navController) }
        composable(NavigationItem.DogBreedDetails.route) { backStackEntry ->
            val dogId = backStackEntry.arguments?.getString("dogId")
            DogDetailsScreen(dogId = dogId ?: "", navController)
        }
    }
}
