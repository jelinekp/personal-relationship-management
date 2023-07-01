package cz.wz.jelinekp.prm.features.signin.ui.signin

import androidx.lifecycle.ViewModel
import cz.wz.jelinekp.prm.features.signin.data.SignInResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel() : ViewModel() {

    private val _screenStateStream = MutableStateFlow(SignInScreenState())
    val screenStateStream get() = _screenStateStream.asStateFlow()
    
    fun onSignInResult(signInResult: SignInResult) {
        _screenStateStream.update { it.copy(
            isSignedIn = signInResult.data != null,
            signInError = signInResult.errorMessage
        ) }
    }
    
    fun resetState() {
        _screenStateStream.update { SignInScreenState() }
    }
}

data class SignInScreenState(
    val id: String? = null,
    val name: String? = null,
    val isSignedIn: Boolean = false,
    val signInError: String? = null,
)