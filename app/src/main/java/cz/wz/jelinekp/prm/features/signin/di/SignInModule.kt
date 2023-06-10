package cz.wz.jelinekp.prm.features.signin.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import cz.wz.jelinekp.prm.features.signin.data.UserRepository
import cz.wz.jelinekp.prm.features.signin.ui.SignInViewModel

val signInModule = module {
    singleOf(::UserRepository)
    viewModelOf(::SignInViewModel)
}