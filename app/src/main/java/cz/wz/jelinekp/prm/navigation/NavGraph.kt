package cz.wz.jelinekp.prm.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.wz.jelinekp.prm.features.contacts.ui.editcontact.EditContactScreen
import cz.wz.jelinekp.prm.features.contacts.ui.list.ContactsScreen

@Composable
fun PrmNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.ContactsScreen.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.ContactsScreen.route) {
            ContactsScreen(
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


