package cz.wz.jelinekp.prm.features.categories.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

	@Query("SELECT * FROM category")
	fun getAllCategories() : Flow<List<DbCategory>>
	
	@Transaction
	@Query("SELECT * FROM contact WHERE id = :contactId")
	fun getCategoriesOfContact(contactId: Long) : Flow<DbContactWithDbCategories>
	
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertCategory(category: DbCategory)
	
	@Update
	suspend fun updateCategory(category: DbCategory)
	
	@Delete
	suspend fun deleteCategory(category: DbCategory)
	
	@Query("DELETE FROM category")
	suspend fun deleteAll()
	
}