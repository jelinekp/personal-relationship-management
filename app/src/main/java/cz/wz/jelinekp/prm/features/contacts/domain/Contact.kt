package cz.wz.jelinekp.prm.features.contacts.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlinx.parcelize.Parcelize

@Entity(tableName = "contact")
@Parcelize
data class Contact (

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

}

val emptyContact = Contact(id = 0, name = "", country = "", contactMethod = "", note = "")