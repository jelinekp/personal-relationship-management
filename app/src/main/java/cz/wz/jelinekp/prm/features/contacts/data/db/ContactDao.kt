package cz.wz.jelinekp.prm.features.contacts.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cz.wz.jelinekp.prm.features.contacts.domain.Contact
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface ContactDao {
	
	@Query("SELECT * FROM contact ORDER BY last_contacted ASC")
	fun getAllContacts() : Flow<List<Contact>>

	@Query("SELECT * FROM contact WHERE id = :id")
	fun getContact(id: Long): Flow<Contact?>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(contacts: List<Contact>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertContact(contact: Contact)

	@Update
	suspend fun updateContact(contact: Contact)

	@Query("UPDATE contact SET last_contacted = :lastContacted WHERE id = :contactId")
	suspend fun updateLastContacted(contactId: Long, lastContacted: LocalDateTime)

	@Delete
	suspend fun delete(contact: Contact)

	@Query("DELETE FROM contact WHERE id = :contactId")
	suspend fun deleteContactById(contactId: Long)

	@Query("DELETE FROM contact")
	suspend fun deleteAll()
}