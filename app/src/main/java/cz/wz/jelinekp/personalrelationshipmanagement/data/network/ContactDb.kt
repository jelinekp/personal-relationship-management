package cz.wz.jelinekp.personalrelationshipmanagement.data.network

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.wz.jelinekp.personalrelationshipmanagement.domain.model.Contact

@Database(entities = [Contact::class], version = 1, exportSchema = false)
abstract class ContactDb : RoomDatabase() {
	abstract fun contactDao(): ContactDao
}