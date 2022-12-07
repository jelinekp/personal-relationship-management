package cz.wz.jelinekp.personalrelationshipmanagement.domain.repository

import cz.wz.jelinekp.personalrelationshipmanagement.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
	fun getAllContactsFromRoom(): Flow<List<Contact>>
	
	fun addContactToRoom(contact: Contact)
}