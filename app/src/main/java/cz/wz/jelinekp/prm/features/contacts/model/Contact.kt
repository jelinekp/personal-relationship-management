package cz.wz.jelinekp.prm.features.contacts.model

import cz.wz.jelinekp.prm.features.contacts.data.db.DbContact
import cz.wz.jelinekp.prm.features.contacts.data.db.toString
import cz.wz.jelinekp.prm.features.contacts.data.firebase.FirebaseContact
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class Contact (

	val id: Long = 0,
	val name: String,
	val categories: List<ContactCategory>,
	val lastContacted: LocalDateTime = LocalDateTime.now(),
	val country: String?,
	val contactMethod: String?,
	val note: String?,
	val modified: LocalDateTime = LocalDateTime.now(),

) {

	val createdDateFormatted : String
		get() = lastContacted.format(DateTimeFormatter.ofPattern("d. L. yyyy"))

	companion object {
		val emptyContact = Contact(id = 0, name = "", categories = listOf(ContactCategory.Other), country = "", contactMethod = "", note = "")
	}

	fun toDbContact() = DbContact(
		id = id,
		name = name,
		category = categories,
		lastContacted = lastContacted,
		country = country,
		contactMethod = contactMethod,
		note = note,
		modified = LocalDateTime.now(),
	)

    fun toFirebaseContact() = FirebaseContact(
		id = id,
		name = name,
		categories = categories.toString,
		lastContacted = lastContacted.toEpochSecond(ZoneOffset.UTC),
		country = country,
		contactMethod = contactMethod,
		note = note,
		modified = modified.toEpochSecond(ZoneOffset.UTC),
	)

}