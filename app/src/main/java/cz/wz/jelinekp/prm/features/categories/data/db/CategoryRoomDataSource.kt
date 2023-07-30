package cz.wz.jelinekp.prm.features.categories.data.db

import cz.wz.jelinekp.prm.features.categories.data.CategoryLocalDataSource
import cz.wz.jelinekp.prm.features.categories.model.Category
import cz.wz.jelinekp.prm.features.contacts.model.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRoomDataSource(
	private val categoryDao: CategoryDao
) : CategoryLocalDataSource {
	override fun getAllCategoriesFlow(): Flow<List<Category>> {
		return categoryDao.getAllCategoriesFlow().map { dbCategories -> dbCategories.map { it.toCategory() } }
	}
	
	override suspend fun getAllCategories(): List<Category> {
		return categoryDao.getAllCategories().map { it.toCategory() }
	}
	
	override fun getCategoriesOfContact(contactId: Long): Flow<ContactWithCategories> {
		return categoryDao.getCategoriesOfContact(contactId).map { it?.toContactWithCategories() ?: ContactWithCategories(
			Contact.emptyContact, emptyList()
		) }
	}
	
	override suspend fun insertCategory(category: Category) {
		return categoryDao.insertCategory(category.toDbCategory())
	}
	
	override suspend fun updateCategory(category: Category) {
		return categoryDao.updateCategory(category.toDbCategory())
	}
	
	override suspend fun deleteCategory(category: Category) {
		return categoryDao.deleteCategory(category.toDbCategory())
	}
	
	override suspend fun deleteAll() {
		return categoryDao.deleteAll()
	}
	
	override suspend fun deleteContactCategory(category: Category, contactId: Long) {
		categoryDao.deleteCategoryOfContact(DbContactCategory(contactId, category.categoryName))
	}
	
	override suspend fun insertContactCategory(category: Category, contactId: Long) {
		categoryDao.insertCategoryOfContact(DbContactCategory(contactId, category.categoryName))
	}
	
	override suspend fun insert(syncedCategories: List<Category>) {
		categoryDao.insert(syncedCategories.map { it.toDbCategory() })
	}
}