package cz.wz.jelinekp.personalrelationshipmanagement.navigation

sealed class Screen(val route: String) {
	object ContactsScreen: Screen(ScreenNames.CONTACTS_SCREEN.toString())
}
