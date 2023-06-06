package cz.wz.jelinekp.prm.features.contacts.data.firebase

import com.google.firebase.database.FirebaseDatabase
import cz.wz.jelinekp.prm.features.contacts.domain.Contact

class FirebaseDataStore(
    private val firebaseDatabase: FirebaseDatabase,
) {

    fun setValue(contact: Contact) {
        val contactReference = firebaseDatabase.getReference("FirebaseContacts/${contact.id}")
        val taskResult = contactReference.setValue(contact.toFirebaseContact())
        /*Log.d("Firebase", "Value set $value")
        val databaseKey = databaseReference.child("FirebaseContact").push().key
        if (databaseKey == null) {
            Log.w("TAG", "Couldn't get push key for posts")
            return
        }

        databaseReference.updateChildren(mapOf(Pair(key, value)))*/
    }
}