package cz.wz.jelinekp.prm.features.contacts.ui.addcontact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.wz.jelinekp.prm.features.contacts.data.ContactRepository
import cz.wz.jelinekp.prm.features.contacts.domain.Contact
import cz.wz.jelinekp.prm.features.contacts.domain.emptyContact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddContactViewModel(
    private val contactRepository: ContactRepository
) : ViewModel() {

    //private val emptyContact: Contact(id = 0, name = "", country = "", contactMethod = "", note = "")
    private val _screenStateStream = MutableStateFlow<Contact>(emptyContact)
    val screenStateStream get() = _screenStateStream.asStateFlow()

    fun addContact() = viewModelScope.launch(Dispatchers.IO) {
        contactRepository.addContactToRoom(screenStateStream.value)
    }

    fun updateState(contact: Contact) {
        _screenStateStream.value = contact
    }

}

data class AddContactScreenState(val contact: Contact)