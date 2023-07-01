package cz.wz.jelinekp.prm.features.categories.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import cz.wz.jelinekp.prm.features.categories.data.db.ContactWithCategories
import cz.wz.jelinekp.prm.features.categories.data.db.DbCategory
import cz.wz.jelinekp.prm.features.categories.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryLocalDataSource {
	
	fun getAllCategories() : Flow<List<Category>>
	
	fun getCategoriesOfContact(contactId: Long) : Flow<ContactWithCategories>
	
	suspend fun insertCategory(category: Category)
	
	suspend fun updateCategory(category: Category)
	
	suspend fun deleteCategory(category: Category)
	
	suspend fun deleteAll()
	
}
