package cz.wz.jelinekp.personalrelationshipmanagement.di

import android.content.Context
import androidx.room.Room
import cz.wz.jelinekp.personalrelationshipmanagement.data.network.ContactDao
import cz.wz.jelinekp.personalrelationshipmanagement.data.network.ContactDb
import cz.wz.jelinekp.personalrelationshipmanagement.data.repository.ContactRepositoryImpl
import cz.wz.jelinekp.personalrelationshipmanagement.domain.repository.ContactRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
	@Provides
	fun provideContactDb(
		@ApplicationContext
		context : Context
	) = Room.databaseBuilder(
		context,
		ContactDb::class.java,
		"contacts"
	)
		.fallbackToDestructiveMigration()
		.createFromAsset("database/contacts.db")
		.build()
	
	@Provides
	fun provideBookDao(
		contactDb: ContactDb
	) = contactDb.contactDao()
	
	@Provides
	fun provideBookRepository(
		contactDao: ContactDao
	): ContactRepository = ContactRepositoryImpl(
		contactDao = contactDao
	)
}