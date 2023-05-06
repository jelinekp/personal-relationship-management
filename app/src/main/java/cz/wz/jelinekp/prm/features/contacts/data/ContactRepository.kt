package cz.wz.jelinekp.prm.features.contacts.data

import cz.wz.jelinekp.prm.features.contacts.data.db.ContactDao
import cz.wz.jelinekp.prm.features.contacts.domain.Contact
import kotlinx.coroutines.flow.Flow

class ContactRepository(
	private val contactLocalDataSource: ContactLocalDataSource,
) {
	suspend fun getAllContactsFromRoom(): Flow<List<Contact>> = contactLocalDataSource.getAllContacts()
	
	suspend fun addContactToRoom(contact: Contact) = contactLocalDataSource.insertContact(contact)
}