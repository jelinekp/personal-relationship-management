package cz.wz.jelinekp.prm.features.contacts.di

import cz.wz.jelinekp.prm.core.data.db.ContactDb
import cz.wz.jelinekp.prm.features.contacts.data.ContactLocalDataSource
import cz.wz.jelinekp.prm.features.contacts.data.ContactRepository
import cz.wz.jelinekp.prm.features.contacts.data.db.ContactRoomDataSource
import cz.wz.jelinekp.prm.features.contacts.data.firebase.FirebaseDataStore
import cz.wz.jelinekp.prm.features.contacts.ui.editcontact.EditContactViewModel
import cz.wz.jelinekp.prm.features.contacts.ui.list.ContactListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val contactModule get() = module {

    single { get<ContactDb>().contactDao() }
    factory<ContactLocalDataSource> { ContactRoomDataSource(contactDao = get()) }

    factoryOf(::FirebaseDataStore)
    factoryOf(::ContactRepository)
    
    viewModelOf(::ContactListViewModel)
    viewModelOf(::EditContactViewModel)

}