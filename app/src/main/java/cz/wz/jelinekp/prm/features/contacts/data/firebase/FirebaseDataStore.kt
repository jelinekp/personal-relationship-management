package cz.wz.jelinekp.prm.features.contacts.data.firebase

import android.util.Log
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.FirebaseDatabase
import cz.wz.jelinekp.prm.features.categories.data.CategoryRepository
import cz.wz.jelinekp.prm.features.categories.data.firebase.FirebaseCategory
import cz.wz.jelinekp.prm.features.categories.model.Category
import cz.wz.jelinekp.prm.features.contacts.model.Contact
import cz.wz.jelinekp.prm.features.signin.data.UserRepository
import cz.wz.jelinekp.prm.features.signin.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class FirebaseDataStore(
	private val firebaseDatabase: FirebaseDatabase,
	private val analytics: FirebaseAnalytics,
	private val userRepository: UserRepository,
	private val categoryRepository: CategoryRepository,
) {
	suspend fun addContact(contact: Contact) {
		val user = userRepository.userStream.first() ?: return
		Log.d(TAG, "---------------------- add Contact user ${user.name}")
		val contactReference =
			firebaseDatabase.getReference("$USERS_NODE/${user.id}/$CONTACTS_NODE/${contact.id}")
		val taskResult = contactReference.setValue(contact.toFirebaseContact())
		
		analytics.logEvent(
			CONTACT_ADDED_EVENT_NAME, bundleOf(
				user.id to contact.name
			)
		)
	}
	
	private fun filteredUserFlow(): Flow<User?> = userRepository.userStream.filter {
		it != null
	}
	
	suspend fun syncContactsFromFirebase(): List<Contact> {
		val user = filteredUserFlow().first()!!
		val contactReference =
			firebaseDatabase.getReference("$USERS_NODE/${user.id}/$CONTACTS_NODE")
		val firebaseContactList = contactReference.get().await().children.map { snapshot ->
			snapshot.getValue(FirebaseContact::class.java)!!
		}
		
		firebaseContactList.forEach { firebaseContact ->
			firebaseContact.categories.forEach { firebaseCategory ->
				categoryRepository.insertContactCategory(
					firebaseCategory.toCategory(),
					firebaseContact.id
				)
			}
		}
		
		return firebaseContactList.map { it.toContact() }
	}
	
	suspend fun syncCategoriesFromFirebase(): List<Category> {
		val user = filteredUserFlow().first()!!
		val categoriesReference =
			firebaseDatabase.getReference("$USERS_NODE/${user.id}/$CATEGORIES_NODE")
		val firebaseCategoryList = categoriesReference.get().await().children.map { snapshot ->
			snapshot.getValue(FirebaseCategory::class.java)!!.toCategory()
		}
		return firebaseCategoryList
	}
	
	private suspend fun contactToFirebaseContact(contact: Contact) : FirebaseContact {
		val firebaseContact = contact.toFirebaseContact(
			activeCategories = categoryRepository.getCategoriesOfContact(contact.id) //TODO rewrite to use Contact object categories
				.first().categories.map { it.toFirebaseCategory() }
		)
		Log.d(TAG, "firebaseContactToUpload $firebaseContact")
		return firebaseContact
	}
	
	suspend fun syncToFirebase(contacts: List<Contact>, categories: List<Category>): Boolean {
		val user = userRepository.userStream.first() ?: return false
		val contactsReference = firebaseDatabase.getReference("$USERS_NODE/${user.id}/$CONTACTS_NODE")
		
		val contactsToUpload = contacts.map { contact ->
			contact.toFirebaseContact()
		}
		
		contactsReference.setValue(contactsToUpload)
			.addOnCompleteListener {
				if (it.isSuccessful) {
					Log.d(TAG, "Successfully wrote contacts to Firebase")
				} else {
					Log.e(TAG, "Failed to write contacts to Firebase", it.exception)
				}
			}
		
		val categoriesReference =
			firebaseDatabase.getReference("$USERS_NODE/${user.id}/$CATEGORIES_NODE")
		
		categoriesReference.setValue(categories.map { it.toFirebaseCategory() })
			.addOnCompleteListener {
				if (it.isSuccessful) {
					Log.d(TAG, "Successfully wrote categories to Firebase")
				} else {
					Log.e(TAG, "Failed to write categories to Firebase", it.exception)
				}
			}
		
		return true
	}
	
	companion object {
		private const val USERS_NODE = "users"
		private const val CONTACTS_NODE = "contacts"
		private const val CATEGORIES_NODE = "categories"
		
		private const val CONTACT_ADDED_EVENT_NAME = "contact_added"
		
		private const val TAG = "FirebaseDataStore"
	}
}