package cz.wz.jelinekp.personalrelationshipmanagement.data.network

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cz.wz.jelinekp.personalrelationshipmanagement.domain.model.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
	
	@Query("SELECT * FROM contacts")
	fun getAllContacts() : Flow<List<Contact>>
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertContact(contact: Contact)
	
	@Update
	fun updateContact(contact: Contact)
	
	@Delete
	fun delete(contact: Contact)
}