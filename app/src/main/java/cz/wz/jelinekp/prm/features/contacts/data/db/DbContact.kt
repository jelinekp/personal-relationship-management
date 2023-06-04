package cz.wz.jelinekp.prm.features.contacts.data.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.wz.jelinekp.prm.features.contacts.domain.Contact
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "contact")
@Parcelize
data class DbContact (

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    @ColumnInfo(name = "last_contacted")
    val lastContacted: LocalDateTime = LocalDateTime.now(),

    val country: String?,

    @ColumnInfo(name = "contact_method")
    val contactMethod: String?,

    val note: String?
) : Parcelable {

    val createdDateFormatted : String
        get() = lastContacted.format(DateTimeFormatter.ofPattern("d. L. yyyy"))

    fun toContact() = Contact(
        id = id,
        name = name,
        lastContacted = lastContacted,
        country = country,
        contactMethod = contactMethod,
        note = note,
    )
}