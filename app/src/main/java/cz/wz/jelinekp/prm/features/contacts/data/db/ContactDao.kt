package cz.wz.jelinekp.prm.features.contacts.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface ContactDao {
	
	@Query("SELECT * FROM contact ORDER BY last_contacted ASC")
	fun getAllContacts() : Flow<List<DbContact>>

	@Query("SELECT * FROM contact WHERE id = :id")
	fun getContact(id: Long): Flow<DbContact?>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(contacts: List<DbContact>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertContact(contact: DbContact) : Long

	@Update
	suspend fun updateContact(contact: DbContact)

	@Query("UPDATE contact SET last_contacted = :lastContacted, modified = :modified WHERE id = :contactId")
	suspend fun updateLastContacted(contactId: Long, lastContacted: LocalDateTime, modified: LocalDateTime)

	@Delete
	suspend fun delete(contact: DbContact)

	@Query("DELETE FROM contact WHERE id = :contactId")
	suspend fun deleteContactById(contactId: Long)

	@Query("DELETE FROM contact")
	suspend fun deleteAll()
}