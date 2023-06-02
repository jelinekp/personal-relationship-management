package cz.wz.jelinekp.prm.features.contacts.ui.editcontact

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.wz.jelinekp.prm.features.contacts.data.ContactRepository
import cz.wz.jelinekp.prm.features.contacts.domain.Contact
import cz.wz.jelinekp.prm.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class EditContactViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val contactRepository: ContactRepository
) : ViewModel() {

    private val _screenStateStream = MutableStateFlow<EditContactScreenState>(
        EditContactScreenState(Contact.emptyContact, false)
    )
    val screenStateStream get() = _screenStateStream.asStateFlow()

    init {
        viewModelScope.launch {
            val contactId: String? = savedStateHandle[Screen.EditContactScreen.ID]
            val contactFlow = if (contactId == null || contactId == "null")
                flowOf(Contact.emptyContact)
            else
                contactRepository.getContactById(contactId.toLong())

            if (contactId == null || contactId == "null")
                _screenStateStream.update {
                    it.copy(isAddingNewContact = true)
                }

            contactFlow.collect { contact ->
                _screenStateStream.update {
                    it.copy(contact = contact!!)
                }
            }

        }
    }

    private fun validateInputs() : Boolean {
        if (_screenStateStream.value.contact.lastContacted >= LocalDateTime.now()) {
            _screenStateStream.value = _screenStateStream.value.copy(lastContactedError = true)
            return false
        } else if (_screenStateStream.value.contact.name.isBlank()) {
            _screenStateStream.value = _screenStateStream.value.copy(nameError = true)
            return false
        }
        return true
    }

    fun applyChanges() : Boolean {

        if (validateInputs()) {
            if (_screenStateStream.value.isAddingNewContact)
                viewModelScope.launch { contactRepository.addContactToRoom(screenStateStream.value.contact) }
            else
                viewModelScope.launch { contactRepository.updateContact(screenStateStream.value.contact) }
            return true
        }
        return false
    }

    private fun updateContactState(contact: Contact) {
        _screenStateStream.value = _screenStateStream.value.copy(contact = contact)
    }

    fun updateName(name: String) {
        _screenStateStream.value = _screenStateStream.value.copy(nameError = false)
        updateContactState(_screenStateStream.value.contact.copy(name = name))
    }

    fun updateCountry(country: String) {
        updateContactState(_screenStateStream.value.contact.copy(country = country))
    }

    fun updateContactMethod(contactMethod: String) {
        updateContactState(_screenStateStream.value.contact.copy(contactMethod = contactMethod))
    }

    fun updateNote(note: String) {
        updateContactState(_screenStateStream.value.contact.copy(note = note))
    }

    fun updateLastContacted(lastContacted: LocalDateTime) {
        _screenStateStream.value = _screenStateStream.value.copy(lastContactedError = false)
        updateContactState(_screenStateStream.value.contact.copy(lastContacted = lastContacted))
    }

    fun showLastContactedDatePicker() {
        _screenStateStream.value = _screenStateStream.value.copy(showLastContactedDatePicker = true)
    }

    fun hideLastContactedDatePicker() {
        _screenStateStream.value = _screenStateStream.value.copy(showLastContactedDatePicker = false)
    }

}

data class EditContactScreenState(
    val contact: Contact,
    val showLastContactedDatePicker: Boolean,
    val isAddingNewContact: Boolean = false,
    val nameError: Boolean = false,
    val countryError: Boolean = false,
    val contactMethodError: Boolean = false,
    val noteError: Boolean = false,
    val lastContactedError: Boolean = false,
)