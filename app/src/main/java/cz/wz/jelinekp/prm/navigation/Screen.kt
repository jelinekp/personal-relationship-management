package cz.wz.jelinekp.prm.navigation

sealed class Screen(val route: String) {

	data object ContactsScreen: Screen(ScreenNames.CONTACTS_SCREEN.toString())

	class EditContactScreen(contactId: String): Screen("${ScreenNames.EDIT_CONTACT_SCREEN}/$contactId") {
		companion object {
			const val ID = "id"
		}
	}
}
