package cz.wz.jelinekp.prm.features.contacts.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.wz.jelinekp.prm.features.contacts.data.ContactRepository
import cz.wz.jelinekp.prm.features.contacts.model.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ContactListViewModel(
	private val repository: ContactRepository
) : ViewModel() {

	private val _screenStateStream = MutableStateFlow<ContactListScreenState>(ContactListScreenState.Loading)
	val screenStateStream get() = _screenStateStream.asStateFlow()

	private val _datePickerScreenStateStream = MutableStateFlow(DatePickerScreenState(showLastContactedDatePicker = null))
	val datePickerScreenStateStream get() = _datePickerScreenStateStream.asStateFlow()

	/*private val _expandedContactsScreenState = MutableStateFlow(ExpandedContactsScreenState())*/

	init {
	    viewModelScope.launch {
			repository.getAllContactsFromRoom().collectLatest {
				_screenStateStream.value = ContactListScreenState.Loaded(it)
			}
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
		val contacts: List<Contact>
	) : ContactListScreenState

}

data class DatePickerScreenState(
	val showLastContactedDatePicker: Long? = null
)

data class ExpandedContactsScreenState(
	val expandedContacts: Map<Long, Boolean>
)