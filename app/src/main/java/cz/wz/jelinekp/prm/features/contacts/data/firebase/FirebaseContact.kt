package cz.wz.jelinekp.prm.features.contacts.data.firebase

import cz.wz.jelinekp.prm.features.contacts.data.db.DbContact
import cz.wz.jelinekp.prm.features.contacts.model.Contact
import cz.wz.jelinekp.prm.features.contacts.model.ContactCategory
import java.time.LocalDateTime
import java.time.ZoneOffset

data class FirebaseContact(
    val id: Long = 0,
    val name: String = "",
    val categories: List<ContactCategory> = emptyList(),
    val lastContacted: Long,
    val country: String? = "",
    val contactMethod: String? = "",
    val note: String? = "",
    val modified: Long,
) {
    fun toContact() = Contact(
        id = id,
        name = name,
        categories = categories,
        lastContacted = LocalDateTime.ofEpochSecond(lastContacted, 0, ZoneOffset.UTC),
        country = country,
        contactMethod = contactMethod,
        note = note,
        modified = LocalDateTime.ofEpochSecond(modified, 0, ZoneOffset.UTC),
    )

    fun toDbContact() = DbContact(
        id = id,
        name = name,
        category = categories,
        lastContacted = LocalDateTime.ofEpochSecond(lastContacted, 0, ZoneOffset.UTC),
        country = country,
        contactMethod = contactMethod,
        note = note,
        modified = LocalDateTime.ofEpochSecond(modified, 0, ZoneOffset.UTC),
    )
}