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
}
