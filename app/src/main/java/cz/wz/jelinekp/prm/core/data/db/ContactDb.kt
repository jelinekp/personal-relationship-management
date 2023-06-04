package cz.wz.jelinekp.prm.core.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cz.wz.jelinekp.prm.features.contacts.data.db.ContactDao
import cz.wz.jelinekp.prm.features.contacts.data.db.Converters
import cz.wz.jelinekp.prm.features.contacts.data.db.DbContact

@Database(entities = [DbContact::class], version = 4)
@TypeConverters(Converters::class)
abstract class ContactDb : RoomDatabase() {

	abstract fun contactDao(): ContactDao

	companion object {
		fun provideContactDb(context : Context): ContactDb = Room.databaseBuilder(
			context,
			ContactDb::class.java,
			"contacts"
		)
			//.fallbackToDestructiveMigration()
			//.createFromAsset("database/contacts.db")
			.build()
	}
}