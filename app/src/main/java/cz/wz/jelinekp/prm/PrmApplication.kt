package cz.wz.jelinekp.prm

import android.app.Application
import cz.wz.jelinekp.prm.core.di.coreModule
import cz.wz.jelinekp.prm.features.contacts.di.contactModule
import cz.wz.jelinekp.prm.features.signin.di.signInModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PrmApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PrmApplication)
            modules(coreModule, contactModule, signInModule)
        }
    }
}