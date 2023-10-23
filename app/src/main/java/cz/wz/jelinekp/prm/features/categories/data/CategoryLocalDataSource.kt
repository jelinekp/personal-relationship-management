package cz.wz.jelinekp.prm.features.categories.data

import cz.wz.jelinekp.prm.features.categories.model.Category
import cz.wz.jelinekp.prm.features.contacts.model.Contact
import kotlinx.coroutines.flow.Flow

interface CategoryLocalDataSource {
	
	fun getAllCategoriesFlow() : Flow<List<Category>>
	
	suspend fun getAllCategories() : List<Category>
	
	fun getCategoriesOfContact(contactId: Long) : Flow<Contact>
	
	suspend fun insertCategory(category: Category)
	
	suspend fun updateCategory(category: Category)
	
	suspend fun deleteCategory(category: Category)
	
	suspend fun deleteAll()
	suspend fun deleteContactCategory(category: Category, contactId: Long)
	suspend fun insertContactCategory(category: Category, contactId: Long)
	suspend fun insert(syncedCategories: List<Category>)
	
}
