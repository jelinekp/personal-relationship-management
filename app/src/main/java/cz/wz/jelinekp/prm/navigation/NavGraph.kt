package cz.wz.jelinekp.prm.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.wz.jelinekp.prm.features.contacts.ui.addcontact.AddContactScreen
import cz.wz.jelinekp.prm.features.contacts.ui.list.ContactsScreen

@Composable
fun PrmNavHost(
	modifier: Modifier = Modifier,
	navController: NavHostController = rememberNavController(),
	startDestination: String = ScreenNames.CONTACTS_SCREEN.toString()
) {
	NavHost(
		modifier = modifier,
		navController = navController,
		startDestination = startDestination
	) {
		composable(ScreenNames.CONTACTS_SCREEN.toString()) {
			ContactsScreen(
				{ navController.navigate(ScreenNames.ADD_CONTACT_SCREEN.toString()) }
			)
		}
		composable(ScreenNames.ADD_CONTACT_SCREEN.toString()) {
			AddContactScreen()
		}
	}
}

