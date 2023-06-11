package cz.wz.jelinekp.prm.features.contacts.data.firebase

import android.util.Log
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.FirebaseDatabase
import cz.wz.jelinekp.prm.features.contacts.model.Contact
import cz.wz.jelinekp.prm.features.signin.data.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class FirebaseDataStore(
    private val firebaseDatabase: FirebaseDatabase,
    private val analytics: FirebaseAnalytics,
    private val userRepository: UserRepository,
) {
    suspend fun addContact(contact: Contact) {
        val user = userRepository.userStream.first() ?: return
        Log.d(TAG, "---------------------- add Contact user ${user.name}")
        val contactReference = firebaseDatabase.getReference("$USERS_NODE/${user.id}/$CONTACTS_NODE/${contact.id}")
        val taskResult = contactReference.setValue(contact.toFirebaseContact())

        analytics.logEvent(CONTACT_ADDED_EVENT_NAME, bundleOf(
            user.id to contact.name
        ))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun syncFromFirebase() : List<Contact> {
        var firebaseContacts = emptyList<Contact>()
        userRepository.userStream.collectLatest { user ->
            Log.d(TAG, "---------------------- user ${user?.name}")
            Log.d(TAG, "Firebase path: $USERS_NODE/${user?.id}/$CONTACTS_NODE")
            if (user != null) {
                val contactReference = firebaseDatabase.getReference("$USERS_NODE/${user.id}/$CONTACTS_NODE")
                val firebaseContactList = contactReference.get().await().children.map { snapshot ->
                    snapshot.getValue(FirebaseContact::class.java)!!.toContact()
                }
                Log.d(TAG, "Syncing from FIREBASE $firebaseContactList")
                firebaseContacts = firebaseContactList
            }
        }
        return firebaseContacts
    }

    suspend fun syncToDatabase(allContacts: Flow<List<Contact>>) {
        val user = userRepository.userStream.first() ?: return
        val contactsReference = firebaseDatabase.getReference("$USERS_NODE/${user.id}/$CONTACTS_NODE")
        contactsReference.setValue(allContacts.first())
    }

    companion object {
        private const val USERS_NODE = "users"
        private const val CONTACTS_NODE = "contacts"

        private const val CONTACT_ADDED_EVENT_NAME = "contact_added"

        private const val TAG = "FirebaseDataStore"
    }
}