package cz.wz.jelinekp.prm.features.contacts.data

import android.util.Log
import cz.wz.jelinekp.prm.features.contacts.data.firebase.FirebaseDataStore
import cz.wz.jelinekp.prm.features.contacts.model.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime

class ContactRepository(
	private val contactLocalDataSource: ContactLocalDataSource,
	private val firebaseDataStore: FirebaseDataStore,
) {
	fun getAllContactsFromRoom(): Flow<List<Contact>> {
		return contactLocalDataSource.getAllContacts()/*.combine(firebaseDataStore.syncFromFirebase()) { roomFlow, firebaseFlow ->
			val newDataFromFirebase = firebaseFlow.filter { contact ->
				val correspondingContact = roomFlow.find { it.id == contact.id }
				(correspondingContact?.modified ?: LocalDateTime.MIN) < contact.modified
			}
			Log.d(TAG, "New data from firebase update $newDataFromFirebase")
			contactLocalDataSource.insert(newDataFromFirebase)
			roomFlow
		}*/
	}
	
	suspend fun addContact(contact: Contact) {
		val id = contactLocalDataSource.insertContact(contact)
		// firebaseDataStore.addContact(contact.copy(id = id))
	}

	/*suspend fun syncContactsToFirebase() {
		contactLocalDataSource.getAllContacts().combine(firebaseDataStore.syncFromFirebase()) { roomFlow, firebaseFlow ->
			val newDataFromRoom = roomFlow.filter { contact ->
				val correspondingContact = firebaseFlow.find { it.id == contact.id }
				(correspondingContact?.modified ?: LocalDateTime.MIN) < contact.modified
			}
			Log.d(TAG, "new data from room to upload: $newDataFromRoom")
			firebaseDataStore.syncToFirebase(newDataFromRoom)
		}
		Log.d(TAG, "Contacts synced")
	}*/

	suspend fun syncContactsToFirebase() : Boolean {
		return firebaseDataStore.syncToFirebase(contactLocalDataSource.getAllContacts().first())
	}

	suspend fun syncContactsFromFirebase() : Boolean {
		val syncedContacts = firebaseDataStore.syncFromFirebase()
		Log.d(TAG, "Synced from firebase")
		if (syncedContacts.isEmpty())
			return false
		contactLocalDataSource.insert(syncedContacts)
		return true
	}

	suspend fun updateContactLastContacted(contactId: Long, lastContacted: LocalDateTime)
	= contactLocalDataSource.updateLastContacted(contactId, lastContacted)

	suspend fun deleteContact(contactId: Long) = contactLocalDataSource.deleteContact(contactId)
    fun getContactById(contactId: Long) = contactLocalDataSource.getContact(contactId)
	suspend fun updateContact(contact: Contact) {
		contactLocalDataSource.updateContact(contact)
	}

	companion object {
		private const val TAG = "ContactRepository"
	}
}