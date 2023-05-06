package cz.wz.jelinekp.prm.features.contacts.data

import cz.wz.jelinekp.prm.features.contacts.domain.Contact
import kotlinx.coroutines.flow.Flow

interface ContactLocalDataSource {

    suspend fun getAllContacts(): Flow<List<Contact>>

    suspend fun getContact(id: Int): Flow<Contact?>

    suspend fun insertContact(contact: Contact)

    suspend fun updateContact(contact: Contact)

    suspend fun insert(contacts: List<Contact>)

    suspend fun deleteContact(contact: Contact)

    suspend fun deleteAll()

}
