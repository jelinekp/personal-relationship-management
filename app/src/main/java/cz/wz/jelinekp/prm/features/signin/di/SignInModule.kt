package cz.wz.jelinekp.prm.features.signin.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import cz.wz.jelinekp.prm.features.signin.data.UserRepository
import cz.wz.jelinekp.prm.features.signin.ui.signin.SignInViewModel
import cz.wz.jelinekp.prm.features.signin.ui.profile.ProfileViewModel

val signInModule = module {
    singleOf(::UserRepository)
    viewModelOf(::SignInViewModel)
    viewModelOf(::ProfileViewModel)
}