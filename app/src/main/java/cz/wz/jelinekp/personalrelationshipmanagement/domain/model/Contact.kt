package cz.wz.jelinekp.personalrelationshipmanagement.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "contacts")
data class Contact (
	
	@PrimaryKey(autoGenerate = true)
	val id: Long = 0,
	
	val name: String,
	
	@ColumnInfo(name = "last_contacted")
	val lastContacted: String,
	
	val country: String?,
	
	@ColumnInfo(name = "contact_method")
	val contactMethod: String?,
	
	val note: String?
)