package com.kirwa.dogsbreedsapp.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kirwa.dogsbreedsapp.ui.navigation.NavigationItem
/**
 * A composable function that represents the bottom navigation bar of the app.
 *
 * - Displays a list of navigation items with icons and labels.
 * - Highlights the selected item based on the current destination.
 * - Navigates to the corresponding screen when an item is clicked.
 *
 * @param modifier Modifier to be applied to the NavigationBar.
 * @param navHostController NavController used for navigation.
 * @param bottomNavItems A list of NavigationItem to be displayed in the bottom bar.
 */

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    bottomNavItems: List<NavigationItem>
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = .85f)
    ) {
        bottomNavItems.iterator().forEach { item ->

            val currentDestination =
                navHostController.currentBackStackEntryAsState().value?.destination?.route
            val isSelected = item.route == currentDestination

            NavigationBarItem(
                icon = {
                    item.icon?.let {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title?.let { it1 -> stringResource(it1) }
                        )
                    }
                },
                label = {
                    Text(
                        text = item.title?.let { it1 -> stringResource(it1) } ?: "",
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Gray
                ),
                selected = isSelected,
                onClick = {
                    if (item.route != currentDestination) navHostController.navigate(route = item.route)
                }
            )
        }
    }
}
