package cz.wz.jelinekp.prm.core.di

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import cz.wz.jelinekp.prm.core.data.db.ContactDb
import org.koin.dsl.module

val coreModule get() = module {
    single { ContactDb.provideContactDb(get()) }

    single {
        FirebaseDatabase.getInstance()
    }

    single {
        FirebaseCrashlytics.getInstance()
    }

    single {
        FirebaseAnalytics.getInstance(get())
    }

    single {
        FirebaseAuth.getInstance()
    }

    single {
        FirebaseRemoteConfig.getInstance()
    }
}
