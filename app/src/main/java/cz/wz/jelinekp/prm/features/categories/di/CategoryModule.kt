package cz.wz.jelinekp.prm.features.categories.di

import cz.wz.jelinekp.prm.core.data.db.ContactDb
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val contactModule get() = module {
	
	single { get<ContactDb>().categoryDao() }
	//factory<CategoryLocalDataSource> { CategoryRoomDataSource(categoryDao = get()) }
	
	
	// factoryOf(cz.wz.jelinekp.prm.features.categorie.data.firebase::FirebaseDataStore)
	// factoryOf(cz.wz.jelinekp.prm.features.categories.data::CategoryRepository)
	
}