package cz.wz.jelinekp.prm.features.contacts.ui.list

import androidx.lifecycle.*
import cz.wz.jelinekp.prm.features.contacts.data.ContactRepository
import cz.wz.jelinekp.prm.features.contacts.domain.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ContactListViewModel(
	private val repository: ContactRepository
) : ViewModel() {

	private val _screenStateStream = MutableStateFlow<ContactListScreenState>(ContactListScreenState.Loading)
	val screenStateStream get() = _screenStateStream.asStateFlow()

	private val _datePickerScreenStateStream = MutableStateFlow(DatePickerScreenState(showLastContactedDatePicker = null))
	val datePickerScreenStateStream get() = _datePickerScreenStateStream.asStateFlow()

	init {
	    viewModelScope.launch {
			val result = repository.getAllContactsFromRoom()
			_screenStateStream.value = ContactListScreenState.Loaded(result)
		}
	}

	fun updateLastContacted(contactId: Long, lastContacted: LocalDateTime = LocalDateTime.now()) {
		viewModelScope.launch {
			repository.updateContactLastContacted(contactId = contactId, lastContacted = lastContacted)
		}
	}

	fun deleteContact(contactId: Long) {
		viewModelScope.launch {
			repository.deleteContact(contactId = contactId)
		}
	}

	fun showLastContactedDatePicker(contactId: Long) {
		_datePickerScreenStateStream.value = _datePickerScreenStateStream.value.copy(showLastContactedDatePicker = contactId)
	}

	fun hideLastContactedDatePicker() {
		_datePickerScreenStateStream.value = _datePickerScreenStateStream.value.copy(showLastContactedDatePicker = null)
	}

}

sealed interface ContactListScreenState {

	object Loading : ContactListScreenState

	data class Loaded(
		val contacts: Flow<List<Contact>>
	) : ContactListScreenState

}

data class DatePickerScreenState(
	val showLastContactedDatePicker: Long? = null
)