package cz.wz.jelinekp.prm.features.contacts.data.firebase

import android.util.Log
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.FirebaseDatabase
import cz.wz.jelinekp.prm.features.contacts.model.Contact
import cz.wz.jelinekp.prm.features.signin.data.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class FirebaseDataStore(
    private val firebaseDatabase: FirebaseDatabase,
    private val analytics: FirebaseAnalytics,
    private val userRepository: UserRepository,
) {
    suspend fun addContact(contact: Contact) {
        val user = userRepository.userStream.first() ?: return
        val contactReference = firebaseDatabase.getReference("$USERS_NODE/${user.id}/$CONTACTS_NODE/${contact.id}")
        val taskResult = contactReference.setValue(contact.toFirebaseContact())

        analytics.logEvent(CONTACT_ADDED_EVENT_NAME, bundleOf(
            user.id to contact.name
        ))
        /*Log.d("Firebase", "Value set $value")
        val databaseKey = databaseReference.child("FirebaseContact").push().key
        if (databaseKey == null) {
            Log.w("TAG", "Couldn't get push key for posts")
            return
        }

        databaseReference.updateChildren(mapOf(Pair(key, value)))*/

    }

    suspend fun syncFromFirebase() : List<Contact> {
        val user = userRepository.userStream.first()
        if (user == null) {
            Log.d("USER", "null")
            return emptyList()
        }
        else {
            Log.d("Firebase path", "$USERS_NODE/${user.id}/$CONTACTS_NODE")
            val contactReference = firebaseDatabase.getReference("$USERS_NODE/${user.id}/$CONTACTS_NODE")

            val firebaseContactList = contactReference.get().await().children.map { snapshot ->
                snapshot.getValue(FirebaseContact::class.java)!!.toContact()
            }
            Log.d("Syncing from FIREBASE", firebaseContactList.toString())
            return firebaseContactList
        }

    }

    suspend fun syncToDatabase(allContacts: Flow<List<Contact>>) {

    }

    companion object {
        private const val USERS_NODE = "users"
        private const val CONTACTS_NODE = "contacts"

        private const val CONTACT_ADDED_EVENT_NAME = "contact_added"
    }
}