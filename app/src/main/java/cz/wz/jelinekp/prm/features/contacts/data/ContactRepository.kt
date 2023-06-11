package cz.wz.jelinekp.prm.features.contacts.data

import android.util.Log
import cz.wz.jelinekp.prm.features.contacts.data.firebase.FirebaseDataStore
import cz.wz.jelinekp.prm.features.contacts.model.Contact
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class ContactRepository(
	private val contactLocalDataSource: ContactLocalDataSource,
	private val firebaseDataStore: FirebaseDataStore,
) {
	fun getAllContactsFromRoom(): Flow<List<Contact>> = contactLocalDataSource.getAllContacts()
	
	suspend fun addContact(contact: Contact) {
		val id = contactLocalDataSource.insertContact(contact)
		firebaseDataStore.addContact(contact.copy(id = id))
	}

	suspend fun syncContactsToFirebase() {
		firebaseDataStore.syncToDatabase(contactLocalDataSource.getAllContacts())
	}

	suspend fun syncContactsFromFirebase(): Boolean {
		Log.d(TAG, "calling firebase fetch")
		val addedIds = contactLocalDataSource.insert(firebaseDataStore.syncFromFirebase())
		if (addedIds.isNotEmpty())
			return true
		return false
	}

	suspend fun updateContactLastContacted(contactId: Long, lastContacted: LocalDateTime)
	= contactLocalDataSource.updateLastContacted(contactId, lastContacted)

	suspend fun deleteContact(contactId: Long) = contactLocalDataSource.deleteContact(contactId)
    fun getContactById(contactId: Long) = contactLocalDataSource.getContact(contactId)
	suspend fun updateContact(contact: Contact) = contactLocalDataSource.updateContact(contact)

	companion object {
		private const val TAG = "ContactRepository"
	}
}