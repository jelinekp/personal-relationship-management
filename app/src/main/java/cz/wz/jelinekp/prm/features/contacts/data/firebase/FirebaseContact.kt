package cz.wz.jelinekp.prm.features.contacts.data.firebase

import cz.wz.jelinekp.prm.features.contacts.data.db.DbContact
import cz.wz.jelinekp.prm.features.contacts.model.Categories
import cz.wz.jelinekp.prm.features.contacts.model.Contact
import java.time.LocalDateTime

data class FirebaseContact(
    val id: Long,
    val name: String,
    val category: List<Categories>,
    val lastContacted: LocalDateTime,
    val country: String?,
    val contactMethod: String?,
    val note: String?,
    val modified: LocalDateTime,
) {
    fun toContact() = Contact(
        id = id,
        name = name,
        category = category,
        lastContacted = lastContacted,
        country = country,
        contactMethod = contactMethod,
        note = note
    )

    fun toDbContact() = DbContact(
        id = id,
        name = name,
        category = category,
        lastContacted = lastContacted,
        country = country,
        contactMethod = contactMethod,
        note = note,
        modified = modified,
    )
}
