package cz.wz.jelinekp.prm.features.contacts.data

import cz.wz.jelinekp.prm.features.contacts.domain.Contact
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface ContactLocalDataSource {

    fun getAllContacts(): Flow<List<Contact>>

    fun getContact(id: Int): Flow<Contact?>

    suspend fun insertContact(contact: Contact)

    suspend fun updateContact(contact: Contact)

    suspend fun updateLastContacted(contactId: Long, lastContacted: LocalDateTime)

    suspend fun insert(contacts: List<Contact>)

    suspend fun deleteContact(contactId: Long)

    suspend fun deleteAll()

}
