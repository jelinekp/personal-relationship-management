package cz.wz.jelinekp.prm.features.contacts.data

import cz.wz.jelinekp.prm.features.categories.data.CategoryLocalDataSource
import cz.wz.jelinekp.prm.features.contacts.data.firebase.FirebaseDataStore
import cz.wz.jelinekp.prm.features.contacts.model.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime

class ContactRepository(
	private val contactLocalDataSource: ContactLocalDataSource,
	private val categoryLocalDataSource: CategoryLocalDataSource,
	private val firebaseDataStore: FirebaseDataStore,
) {
	fun getAllContactsFromRoom(): Flow<List<Contact>> {
		return contactLocalDataSource.getAllContacts()
	}
	
	suspend fun addContact(contact: Contact) : Long {
		return contactLocalDataSource.insertContact(contact)
	}
	suspend fun syncContactsToFirebase() : Boolean {
		return firebaseDataStore.syncToFirebase(
			contactLocalDataSource.getAllContacts().first(),
			categoryLocalDataSource.getAllCategories().first()
		)
	}

	suspend fun syncContactsFromFirebase() : Boolean {
		val syncedContacts = firebaseDataStore.syncContactsFromFirebase()
		
		if (syncedContacts.isEmpty())
			return false

		contactLocalDataSource.deleteAll()
		contactLocalDataSource.insert(syncedContacts)
		
		val syncedCategories = firebaseDataStore.syncCategoriesFromFirebase()
		if (syncedCategories.isNotEmpty())
			categoryLocalDataSource.deleteAll()
		categoryLocalDataSource.insert(syncedCategories)
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