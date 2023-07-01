package cz.wz.jelinekp.prm.core.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import cz.wz.jelinekp.prm.features.categories.data.db.CategoryDao
import cz.wz.jelinekp.prm.features.categories.data.db.DbCategory
import cz.wz.jelinekp.prm.features.categories.data.db.DbContactCategory
import cz.wz.jelinekp.prm.features.contacts.data.db.ContactDao
import cz.wz.jelinekp.prm.features.contacts.data.db.Converters
import cz.wz.jelinekp.prm.features.contacts.data.db.DbContact

@Database(entities = [DbContact::class, DbCategory::class, DbContactCategory::class], version = 6)
@TypeConverters(Converters::class)
abstract class ContactDb : RoomDatabase() {

	abstract fun contactDao(): ContactDao
	
	abstract fun categoryDao(): CategoryDao

	companion object {

		private val migration_4_5: Migration = object: Migration(4, 5) {
			override fun migrate(database: SupportSQLiteDatabase) {
				database.execSQL(
					sql = "ALTER TABLE contact ADD COLUMN modified TEXT NOT NULL DEFAULT '1'"
				)
				database.execSQL(
					sql = "ALTER TABLE contact ADD COLUMN category TEXT NOT NULL DEFAULT 'other'"
				)
			}
		}
		fun provideContactDb(context : Context): ContactDb = Room.databaseBuilder(
			context,
			ContactDb::class.java,
			"contacts"
		)
			.fallbackToDestructiveMigration()
			//.createFromAsset("database/contacts.db")
			//.addMigrations(migration_4_5)
			.build()
	}
}