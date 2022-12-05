package cz.wz.jelinekp.personalrelationshipmanagement.data

import androidx.room.RoomDatabase

abstract class PrmDatabase : RoomDatabase() {
	abstract fun prmDao(): PrmDao
	
	companion object {
		@Volatile
		private var INSTANCE: PrmDatabase? = null
	}
}