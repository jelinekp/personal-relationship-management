package cz.wz.jelinekp.prm.features.categories.di

import cz.wz.jelinekp.prm.core.data.db.ContactDb
import cz.wz.jelinekp.prm.features.categories.data.CategoryLocalDataSource
import cz.wz.jelinekp.prm.features.categories.data.db.CategoryRoomDataSource
import cz.wz.jelinekp.prm.features.categories.data.CategoryRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val categoryModule get() = module {
	
	single { get<ContactDb>().categoryDao() }
	factory<CategoryLocalDataSource> { CategoryRoomDataSource(categoryDao = get()) }
	
	// factoryOf(cz.wz.jelinekp.prm.features.categories.data.firebase::FirebaseDataStore)
	factoryOf(::CategoryRepository)
	
}