package cz.wz.jelinekp.prm.features.categories.data

import cz.wz.jelinekp.prm.features.categories.model.Category
import cz.wz.jelinekp.prm.features.contacts.model.Contact
import kotlinx.coroutines.flow.Flow

class CategoryRepository(
	private val categoryLocalDataSource: CategoryLocalDataSource
) {
	fun getAllCategoryFromRoom() : Flow<List<Category>> {
		return categoryLocalDataSource.getAllCategoriesFlow()
	}
	
	fun getCategoriesOfContact(contactId: Long) : Flow<Contact> {
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
	
	suspend fun deleteContactCategory(category: Category, contactId: Long) {
		return categoryLocalDataSource.deleteContactCategory(category, contactId)
	}
	
	suspend fun insertContactCategory(category: Category, contactId: Long) {
		return categoryLocalDataSource.insertContactCategory(category, contactId)
	}
}