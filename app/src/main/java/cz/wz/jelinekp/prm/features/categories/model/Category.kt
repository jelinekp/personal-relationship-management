package cz.wz.jelinekp.prm.features.categories.model

import cz.wz.jelinekp.prm.features.categories.data.db.DbCategory
import cz.wz.jelinekp.prm.features.categories.data.firebase.FirebaseCategory

data class Category(
	val categoryName: String
) {
	fun toDbCategory(): DbCategory {
		return DbCategory(categoryName = categoryName)
	}
	
	fun toFirebaseCategory() : FirebaseCategory {
		return FirebaseCategory(categoryName = categoryName)
	}
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Category) return false
		return this.categoryName == other.categoryName
	}
	
	override fun hashCode(): Int {
		return categoryName.hashCode()
	}
}
