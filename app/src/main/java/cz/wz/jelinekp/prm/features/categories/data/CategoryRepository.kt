package cz.wz.jelinekp.prm.features.categories.data

import cz.wz.jelinekp.prm.features.categories.data.db.ContactWithCategories
import cz.wz.jelinekp.prm.features.categories.model.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepository(
	private val categoryLocalDataSource: CategoryLocalDataSource
) {
	fun getAllCategoryFromRoom() : Flow<List<Category>> {
		return categoryLocalDataSource.getAllCategories()
	}
	
	fun getCategoriesOfContact(contactId: Long) : Flow<ContactWithCategories> {
		return categoryLocalDataSource.getCategoriesOfContact(contactId)
	}
	
	suspend fun insertCategory(category: Category) {
		return categoryLocalDataSource.insertCategory(category)
	}
	
	suspend fun updateCategory(category: Category) {
		return categoryLocalDataSource.updateCategory(category)
	}
	
	suspend fun deleteCategory(category: Category) {
		return categoryLocalDataSource.deleteCategory(category)
	}
	
	suspend fun deleteAll() {
		return categoryLocalDataSource.deleteAll()
	}
}