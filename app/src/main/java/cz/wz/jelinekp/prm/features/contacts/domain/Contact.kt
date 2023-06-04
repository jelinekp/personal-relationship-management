package cz.wz.jelinekp.prm.features.contacts.domain

import cz.wz.jelinekp.prm.features.contacts.data.db.DbContact
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Contact (

	val id: Long = 0,
	val name: String,
	val lastContacted: LocalDateTime = LocalDateTime.now(),
	val country: String?,
	val contactMethod: String?,
	val note: String?

) {

	val createdDateFormatted : String
		get() = lastContacted.format(DateTimeFormatter.ofPattern("d. L. yyyy"))

	companion object {
		val emptyContact = Contact(id = 0, name = "", country = "", contactMethod = "", note = "")
	}

	fun toDbContact() = DbContact(
		id = id,
		name = name,
		lastContacted = lastContacted,
		country = country,
		contactMethod = contactMethod,
		note = note,
	)

}