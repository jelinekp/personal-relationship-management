package cz.wz.jelinekp.prm.features.contacts.ui.editcontact

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.wz.jelinekp.prm.features.contacts.data.ContactRepository
import cz.wz.jelinekp.prm.features.contacts.model.Categories
import cz.wz.jelinekp.prm.features.contacts.model.Contact
import cz.wz.jelinekp.prm.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _validationSharedFlowStream = MutableSharedFlow<EditContactValidationState>()
    val validationSharedFlowStream get() = _validationSharedFlowStream.asSharedFlow()

    private val _editContactFormState = MutableStateFlow(EditContactFormSate())
    val editContactFormSate get() =  _editContactFormState.asStateFlow()

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
            viewModelScope.launch {
                _validationSharedFlowStream.emit(EditContactValidationState(isLastContactedError = true))
            }
            return false
        } else if (_screenStateStream.value.contact.name.isBlank()) {
            viewModelScope.launch {
                _validationSharedFlowStream.emit(EditContactValidationState(isNameError = true))
            }
            return false
        } else if (_screenStateStream.value.contact.category.isEmpty()) {
            viewModelScope.launch {
                _validationSharedFlowStream.emit(EditContactValidationState(isCategoryError = true))
            }
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
        viewModelScope.launch {
            _validationSharedFlowStream.emit(EditContactValidationState(isNameError = false))
        }
        updateContactState(_screenStateStream.value.contact.copy(name = name))
    }

    fun updateCategory(category: List<Categories>) {
        viewModelScope.launch {
            _validationSharedFlowStream.emit(EditContactValidationState(isCategoryError = false))
        }
        updateContactState(_screenStateStream.value.contact.copy(category = category))
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
        viewModelScope.launch {
            _validationSharedFlowStream.emit(EditContactValidationState(isLastContactedError = false))
        }
        updateContactState(_screenStateStream.value.contact.copy(lastContacted = lastContacted))
    }

    fun showCategoryDropDown(isExpanded: Boolean) {
        _editContactFormState.update {
            it.copy(isCategoryDropDownExpanded = isExpanded)
        }
    }

    fun showLastContactedDatePicker(isShowing: Boolean) {
        _screenStateStream.value = _screenStateStream.value.copy(isShowingLastContactedDatePicker = isShowing)
    }

}

data class EditContactScreenState(
    val contact: Contact,
    val isShowingLastContactedDatePicker: Boolean,
    val isAddingNewContact: Boolean = false,
)

data class EditContactValidationState(
    val isNameError: Boolean = false,
    val isCategoryError: Boolean = false,
    val isCountryError: Boolean = false,
    val isContactMethodError: Boolean = false,
    val isNoteError: Boolean = false,
    val isLastContactedError: Boolean = false,
)

data class EditContactFormSate(
    val isCategoryDropDownExpanded: Boolean = false,

)