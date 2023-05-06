package cz.wz.jelinekp.prm.core.di

import cz.wz.jelinekp.prm.core.data.db.ContactDb
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule get() = module {
    single { ContactDb.provideContactDb(androidContext()) }
}
