package cz.wz.jelinekp.prm.core.di

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import cz.wz.jelinekp.prm.core.data.db.ContactDb
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule get() = module {
    single { ContactDb.provideContactDb(androidContext()) }

    single {
        FirebaseCrashlytics.getInstance()
    }

    single {
        FirebaseAnalytics.getInstance(androidContext())
    }

    single {
        FirebaseAuth.getInstance()
    }

    single {
        FirebaseRemoteConfig.getInstance()
    }
}
