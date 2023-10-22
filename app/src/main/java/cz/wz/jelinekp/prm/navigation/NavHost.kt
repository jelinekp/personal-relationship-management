package cz.wz.jelinekp.prm.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.wz.jelinekp.prm.features.contacts.ui.editcontact.EditContactScreen
import cz.wz.jelinekp.prm.features.contacts.ui.list.ContactListScreen

@Composable
fun PrmNavHost(
    modifier: Modifier = Modifier,
    navBarHeight: Int = 0,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.ContactsScreen.route
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val realNavBarHeight = if (isPortrait) navBarHeight else 18

    NavHost(
        modifier = modifier.padding(top = realNavBarHeight.dp),
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Screen.ContactsScreen.route,
        ) {
            ContactListScreen(
                onNavigateToAddContact = { navController.navigate(Screen.EditContactScreen(it.toString()).route) }
            )
        }
        composable(
            route = Screen.EditContactScreen("{${Screen.EditContactScreen.ID}}").route,
            arguments = listOf(
                navArgument(name = Screen.EditContactScreen.ID) {
                    type = NavType.StringType
                },
            ),
        ) {
            EditContactScreen(
                navigateUp = { navController.navigateUp() },
            )
        }
    }
}


