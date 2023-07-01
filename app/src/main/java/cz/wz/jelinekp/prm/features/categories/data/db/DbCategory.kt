package cz.wz.jelinekp.prm.features.categories.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.wz.jelinekp.prm.features.categories.data.firebase.FirebaseCategory
import cz.wz.jelinekp.prm.features.categories.model.Category

@Entity(tableName = "Category")
data class DbCategory (
	@PrimaryKey
	@ColumnInfo(name = "category_name")
	val categoryName: String
) {
	fun toCategory(): Category {
		return Category(categoryName = categoryName)
	}
	
	fun toFirebaseCategory() : FirebaseCategory {
		return FirebaseCategory(categoryName = categoryName)
	}
}