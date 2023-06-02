package cz.wz.jelinekp.prm.features.contacts.ui.addcontact

import android.content.Context
import android.text.BoringLayout
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.wz.jelinekp.prm.features.contacts.data.ContactRepository
import cz.wz.jelinekp.prm.features.contacts.domain.Contact
import cz.wz.jelinekp.prm.features.contacts.domain.Contact.Companion.emptyContact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.definition._createDefinition
import java.time.LocalDateTime

class AddContactViewModel(
    private val contactRepository: ContactRepository
) : ViewModel() {

    private val _screenStateStream = MutableStateFlow<AddContactScreenState>(AddContactScreenState(emptyContact, false))
    val screenStateStream get() = _screenStateStream.asStateFlow()

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
    fun addContact() = viewModelScope.launch(Dispatchers.IO) {
        if (validateInputs()) {
            contactRepository.addContactToRoom(screenStateStream.value.contact)
        }
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

data class AddContactScreenState(
    val contact: Contact,
    val showLastContactedDatePicker: Boolean,
    val nameError: Boolean = false,
    val countryError: Boolean = false,
    val contactMethodError: Boolean = false,
    val noteError: Boolean = false,
    val lastContactedError: Boolean = false,
    )