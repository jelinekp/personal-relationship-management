package cz.wz.jelinekp.prm.features.contacts.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.wz.jelinekp.prm.features.categories.data.CategoryRepository
import cz.wz.jelinekp.prm.features.categories.model.Category
import cz.wz.jelinekp.prm.features.contacts.data.ContactRepository
import cz.wz.jelinekp.prm.features.contacts.model.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ContactListViewModel(
	private val contactRepository: ContactRepository,
	private val categoryRepository: CategoryRepository,
) : ViewModel() {

	private val _screenStateStream = MutableStateFlow<ContactListScreenState>(ContactListScreenState.Loading)
	val screenStateStream get() = _screenStateStream.asStateFlow()

	private val _datePickerScreenStateStream = MutableStateFlow(DatePickerScreenState(showLastContactedDatePicker = null))
	val datePickerScreenStateStream get() = _datePickerScreenStateStream.asStateFlow()
	
	init {
		viewModelScope.launch {
			val combinedFlow = contactRepository.getAllContactsFromRoom()
				.combine(categoryRepository.getAllCategoryFromRoom()) { contacts, categories ->
					ContactListScreenState.Loaded(contacts = contacts, categories = categories, filteredContacts = contacts)
				}

			combinedFlow.collectLatest {
				_screenStateStream.value = it
			}
		}
	}

	fun updateLastContacted(contactId: Long, lastContacted: LocalDateTime = LocalDateTime.now()) {
		viewModelScope.launch {
			contactRepository.updateContactLastContacted(contactId = contactId, lastContacted = lastContacted)
		}
	}

	fun deleteContact(contactId: Long) {
		viewModelScope.launch {
			contactRepository.deleteContact(contactId = contactId)
		}
	}

	fun showLastContactedDatePicker(contactId: Long) {
		_datePickerScreenStateStream.value = _datePickerScreenStateStream.value.copy(showLastContactedDatePicker = contactId)
	}

	fun hideLastContactedDatePicker() {
		_datePickerScreenStateStream.value = _datePickerScreenStateStream.value.copy(showLastContactedDatePicker = null)
	}

	fun filterByCategory(categoryName: String) {
		viewModelScope.launch {
			val loadedState = _screenStateStream.value as? ContactListScreenState.Loaded ?: return@launch
			val filteredContacts = if (categoryName == "all") {
				loadedState.contacts
			} else {
				loadedState.contacts.filter { contact ->
					 contact.categories.map { it.toString() }.contains(categoryName)
				}
			}
			_screenStateStream.value = loadedState.copy(filteredContacts = filteredContacts, filteredCategory = categoryName)
		}
	}

}

sealed interface ContactListScreenState {

	data object Loading : ContactListScreenState

	data class Loaded(
		val contacts: List<Contact>,
		val categories: List<Category>,
		val filteredCategory: String = "all",
		val filteredContacts: List<Contact>,
	) : ContactListScreenState

}

data class DatePickerScreenState(
	val showLastContactedDatePicker: Long? = null
)

data class ExpandedContactsScreenState(
	val expandedContacts: Map<Long, Boolean>
)