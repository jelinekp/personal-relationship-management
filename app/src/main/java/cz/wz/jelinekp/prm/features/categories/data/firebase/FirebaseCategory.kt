package cz.wz.jelinekp.prm.features.categories.data.firebase

import cz.wz.jelinekp.prm.features.categories.data.db.DbCategory
import cz.wz.jelinekp.prm.features.categories.model.Category

data class FirebaseCategory(
	val categoryName: String = ""
) {
	fun toDbCategory(): DbCategory {
		return DbCategory(categoryName = categoryName)
	}
	
	fun toCategory() : Category {
		return Category(categoryName = categoryName)
	}
}