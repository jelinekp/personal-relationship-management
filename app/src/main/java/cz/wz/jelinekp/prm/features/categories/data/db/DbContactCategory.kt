package cz.wz.jelinekp.prm.features.categories.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_category", primaryKeys = ["contact_id", "category_name"])
data class DbContactCategory(
	
	@ColumnInfo(name = "contact_id")
	val contactId: Long,
	
	@ColumnInfo(name = "category_name")
	val categoryName: String,
	
)
