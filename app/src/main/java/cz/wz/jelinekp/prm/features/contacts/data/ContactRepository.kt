package cz.wz.jelinekp.prm.features.contacts.data

import cz.wz.jelinekp.prm.features.contacts.data.firebase.FirebaseDataStore
import cz.wz.jelinekp.prm.features.contacts.domain.Contact
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class ContactRepository(
	private val contactLocalDataSource: ContactLocalDataSource,
	private val firebaseDataStore: FirebaseDataStore,
) {
	fun getAllContactsFromRoom(): Flow<List<Contact>> = contactLocalDataSource.getAllContacts()
	
	suspend fun addContactToRoom(contact: Contact) {
		contactLocalDataSource.insertContact(contact)
		firebaseDataStore.setValue(contact)
	}

	suspend fun updateContactLastContacted(contactId: Long, lastContacted: LocalDateTime)
	= contactLocalDataSource.updateLastContacted(contactId, lastContacted)

	suspend fun deleteContact(contactId: Long) = contactLocalDataSource.deleteContact(contactId)
    fun getContactById(contactId: Long) = contactLocalDataSource.getContact(contactId)
	suspend fun updateContact(contact: Contact) = contactLocalDataSource.updateContact(contact)
}