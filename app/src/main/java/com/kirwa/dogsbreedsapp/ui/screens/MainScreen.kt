package com.kirwa.dogsbreedsapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kirwa.dogsbreedsapp.R
import com.kirwa.dogsbreedsapp.ui.component.BottomNavBar
import com.kirwa.dogsbreedsapp.ui.navigation.Navigation
import com.kirwa.dogsbreedsapp.ui.navigation.NavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navHostController = rememberNavController()

    val topLevelDestinations = listOf(
        NavigationItem.Home,
        NavigationItem.Favourites
    )

    val currentDestination = navHostController.currentBackStackEntryAsState().value?.destination?.route.orEmpty()
    val isTopLevelDestination = currentDestination in topLevelDestinations.map { it.route }

    val appBarTitle = stringResource(R.string.app_name)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(appBarTitle, style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            if (isTopLevelDestination) {
                BottomNavBar(
                    bottomNavItems = topLevelDestinations,
                    navHostController = navHostController
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Navigation(navHostController)  // Uses your Navigation composable
        }
    }
}
