package cz.wz.jelinekp.prm.features.categories.data

import cz.wz.jelinekp.prm.features.categories.data.db.ContactWithCategories
import cz.wz.jelinekp.prm.features.categories.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryLocalDataSource {
	
	fun getAllCategoriesFlow() : Flow<List<Category>>
	
	suspend fun getAllCategories() : List<Category>
	
	fun getCategoriesOfContact(contactId: Long) : Flow<ContactWithCategories>
	
	suspend fun insertCategory(category: Category)
	
	suspend fun updateCategory(category: Category)
	
	suspend fun deleteCategory(category: Category)
	
	suspend fun deleteAll()
	suspend fun deleteContactCategory(category: Category, contactId: Long)
	suspend fun insertContactCategory(category: Category, contactId: Long)
	suspend fun insert(syncedCategories: List<Category>)
	
}
