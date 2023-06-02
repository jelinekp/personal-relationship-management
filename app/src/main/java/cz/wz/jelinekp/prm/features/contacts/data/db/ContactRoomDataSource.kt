package cz.wz.jelinekp.prm.features.contacts.data.db

import cz.wz.jelinekp.prm.features.contacts.data.ContactLocalDataSource
import cz.wz.jelinekp.prm.features.contacts.domain.Contact
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class ContactRoomDataSource(private val contactDao: ContactDao) : ContactLocalDataSource {

    override fun getAllContacts(): Flow<List<Contact>> = contactDao.getAllContacts()

    override fun getContact(id: Long): Flow<Contact?> = contactDao.getContact(id)

    override suspend fun insertContact(contact: Contact) = contactDao.insertContact(contact)

    override suspend fun updateContact(contact: Contact) = contactDao.updateContact(contact)
    override suspend fun updateLastContacted(contactId: Long, lastContacted: LocalDateTime)
    = contactDao.updateLastContacted(contactId, lastContacted)

    override suspend fun insert(contacts: List<Contact>) = contactDao.insert(contacts)

    override suspend fun deleteContact(contactId: Long) = contactDao.deleteContactById(contactId)

    override suspend fun deleteAll() = contactDao.deleteAll()
}