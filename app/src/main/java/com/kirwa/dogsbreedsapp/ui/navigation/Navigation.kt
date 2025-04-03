package com.kirwa.dogsbreedsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedDetails.DogBreedDetailsScreen
import com.kirwa.dogsbreedsapp.ui.screens.dogBreedsList.HomeScreen
import com.kirwa.dogsbreedsapp.ui.screens.favouriteDogBreeds.FavouriteScreen
/**
 * Sets up navigation for the app using a NavHost.
 *
 * - Defines the start destination as the Home screen.
 * - Provides navigation to the Home, Favourites, and Dog Breed Details screens.
 * - Passes necessary arguments to the DogBreedDetailsScreen.
 */

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) { HomeScreen(navController) }
        composable(NavigationItem.Favourites.route) { FavouriteScreen(navController) }
        composable(NavigationItem.DogBreedDetails.route) { backStackEntry ->
            val dogId = backStackEntry.arguments?.getString("dogId")
            DogBreedDetailsScreen(dogId = dogId ?: "", navController)
        }
    }
}
