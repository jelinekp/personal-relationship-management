package cz.wz.jelinekp.prm.features.contacts.data.db

import cz.wz.jelinekp.prm.features.contacts.data.ContactLocalDataSource
import cz.wz.jelinekp.prm.features.contacts.domain.Contact
import kotlinx.coroutines.flow.Flow

class ContactRoomDataSource(private val contactDao: ContactDao) : ContactLocalDataSource {

    override suspend fun getAllContacts(): Flow<List<Contact>> = contactDao.getAllContacts()

    override suspend fun getContact(id: Int): Flow<Contact?> = contactDao.getContact(id)

    override suspend fun insertContact(contact: Contact) = contactDao.insertContact(contact)

    override suspend fun updateContact(contact: Contact) = contactDao.updateContact(contact)

    override suspend fun insert(contacts: List<Contact>) = contactDao.insert(contacts)

    override suspend fun deleteContact(contact: Contact) = contactDao.delete(contact)

    override suspend fun deleteAll() = contactDao.deleteAll()
}