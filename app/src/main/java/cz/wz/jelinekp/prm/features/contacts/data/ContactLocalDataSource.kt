package cz.wz.jelinekp.prm.features.contacts.data

import cz.wz.jelinekp.prm.features.contacts.model.Contact
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface ContactLocalDataSource {

    fun getAllContactsFlow(): Flow<List<Contact>>
    
    suspend fun getAllContacts(): List<Contact>

    fun getContact(id: Long): Flow<Contact?>

    suspend fun insertContact(contact: Contact) : Long

    suspend fun updateContact(contact: Contact)

    suspend fun updateLastContacted(contactId: Long, lastContacted: LocalDateTime, modified: LocalDateTime = LocalDateTime.now())

    suspend fun insert(contacts: List<Contact>): List<Long>

    suspend fun deleteContact(contactId: Long)

    suspend fun deleteAll()

}
