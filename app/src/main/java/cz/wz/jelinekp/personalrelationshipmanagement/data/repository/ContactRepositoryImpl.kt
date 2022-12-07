package cz.wz.jelinekp.personalrelationshipmanagement.data.repository

import cz.wz.jelinekp.personalrelationshipmanagement.data.network.ContactDao
import cz.wz.jelinekp.personalrelationshipmanagement.domain.model.Contact
import cz.wz.jelinekp.personalrelationshipmanagement.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow

class ContactRepositoryImpl(
	private val contactDao: ContactDao
) : ContactRepository {
	override fun getAllContactsFromRoom(): Flow<List<Contact>> = contactDao.getAllContacts()
	
	override fun addContactToRoom(contact: Contact) = contactDao.insertContact(contact)
}