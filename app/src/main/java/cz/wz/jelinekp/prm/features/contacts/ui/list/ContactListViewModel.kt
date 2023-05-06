package cz.wz.jelinekp.prm.features.contacts.ui.list

import androidx.lifecycle.*
import cz.wz.jelinekp.prm.features.contacts.data.ContactRepository
import cz.wz.jelinekp.prm.features.contacts.domain.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactListViewModel(
	private val repository: ContactRepository
) : ViewModel() {

	private val _screenStateStream = MutableStateFlow<ContactListScreenState>(ContactListScreenState.Loading)
	val screenStateStream get() = _screenStateStream.asStateFlow()

	init {
	    viewModelScope.launch {
			val result = repository.getAllContactsFromRoom()
			_screenStateStream.value = ContactListScreenState.Loaded(result)
		}
	}

}

sealed interface ContactListScreenState {

	object Loading : ContactListScreenState

	data class Loaded(
		val contacts: Flow<List<Contact>>
	) : ContactListScreenState

}