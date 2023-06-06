package cz.wz.jelinekp.prm.features.contacts.data.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.wz.jelinekp.prm.features.contacts.data.firebase.FirebaseContact
import cz.wz.jelinekp.prm.features.contacts.domain.Contact
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Entity(tableName = "contact")
@Parcelize
data class DbContact (

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val category: String,

    @ColumnInfo(name = "last_contacted")
    val lastContacted: LocalDateTime = LocalDateTime.now(),

    val country: String?,

    @ColumnInfo(name = "contact_method")
    val contactMethod: String?,

    val note: String?,

    val modified: LocalDateTime = LocalDateTime.now(),

) : Parcelable {

    fun toContact() = Contact(
        id = id,
        name = name,
        category = category,
        lastContacted = lastContacted,
        country = country,
        contactMethod = contactMethod,
        note = note,
    )

    fun toFirebaseContact() = FirebaseContact(
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