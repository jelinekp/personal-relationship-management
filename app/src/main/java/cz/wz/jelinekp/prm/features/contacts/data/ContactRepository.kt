package cz.wz.jelinekp.prm.features.contacts.data

import cz.wz.jelinekp.prm.features.contacts.domain.Contact
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class ContactRepository(
	private val contactLocalDataSource: ContactLocalDataSource,
) {
	fun getAllContactsFromRoom(): Flow<List<Contact>> = contactLocalDataSource.getAllContacts()
	
	suspend fun addContactToRoom(contact: Contact) = contactLocalDataSource.insertContact(contact)

	suspend fun updateContactLastContacted(contactId: Long, lastContacted: LocalDateTime)
	= contactLocalDataSource.updateLastContacted(contactId, lastContacted)

	suspend fun deleteContact(contactId: Long) = contactLocalDataSource.deleteContact(contactId)
}