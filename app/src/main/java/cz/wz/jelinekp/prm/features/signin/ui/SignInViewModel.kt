package cz.wz.jelinekp.prm.features.signin.ui

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.wz.jelinekp.prm.features.signin.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class SignInViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _screenStateStream = MutableStateFlow(ProfileScreenState())
    val screenStateStream get() = _screenStateStream.asStateFlow()

    init {
        userRepository.userStream
            .onEach { user ->
                _screenStateStream.update { state ->
                    state.copy(id = user?.id, name = user?.name)
                }
            }
            .launchIn(viewModelScope)
    }

    fun signIn(activity: Activity) {
        userRepository.signIn(activity)
    }

    fun signOut() {
        userRepository.signOut()
    }
}

data class ProfileScreenState(
    val id: String? = null,
    val name: String? = null,
)