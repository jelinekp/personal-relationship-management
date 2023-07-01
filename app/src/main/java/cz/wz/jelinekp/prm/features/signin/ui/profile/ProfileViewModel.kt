package cz.wz.jelinekp.prm.features.signin.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.wz.jelinekp.prm.features.contacts.data.ContactRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
	private val contactRepository: ContactRepository,
) : ViewModel() {
	
	private val _screenState = MutableStateFlow(ProfileScreenState())
	val screenState get() = _screenState.asStateFlow()
	
	fun syncToFirebase() {
		viewModelScope.launch {
			_screenState.update {
				it.copy(isSyncToFirebaseSuccess = contactRepository.syncContactsToFirebase())
			}
			delay(3_000L)
			_screenState.update {
				it.copy(isSyncToFirebaseSuccess = false)
			}
		}
	}
	
	fun syncFromFirebase() {
		viewModelScope.launch {
			_screenState.update {
				it.copy(isSyncFromFirebaseSuccess = contactRepository.syncContactsFromFirebase())
			}
			delay(3_000L)
			_screenState.update {
				it.copy(isSyncFromFirebaseSuccess = false)
			}
		}
	}
	
}

data class ProfileScreenState (
	val isSyncToFirebaseSuccess: Boolean = false,
	val isSyncFromFirebaseSuccess: Boolean = false,
)
