package cz.wz.jelinekp.prm.features.contacts.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cz.wz.jelinekp.prm.features.contacts.domain.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
	
	@Query("SELECT * FROM contact")
	fun getAllContacts() : Flow<List<Contact>>

	@Query("SELECT * FROM contact WHERE id = :id")
	fun getContact(id: Int): Flow<Contact?>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(contacts: List<Contact>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertContact(contact: Contact)

	@Update
	suspend fun updateContact(contact: Contact)

	@Delete
	suspend fun delete(contact: Contact)

	@Query("DELETE FROM contact")
	suspend fun deleteAll()
}