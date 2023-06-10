package cz.wz.jelinekp.prm.features.contacts.ui.editcontact

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.wz.jelinekp.prm.features.contacts.data.ContactRepository
import cz.wz.jelinekp.prm.features.contacts.model.Contact
import cz.wz.jelinekp.prm.features.contacts.model.ContactCategory
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
        EditContactScreenState(Contact.emptyContact)
    )
    val screenStateStream get() = _screenStateStream.asStateFlow()

    private val _validationSharedFlowStream = MutableSharedFlow<EditContactValidationState>()
    val validationSharedFlowStream get() = _validationSharedFlowStream.asSharedFlow()

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
        }
        return true
    }

    fun applyChanges() : Boolean {

        if (validateInputs()) {
            if (_screenStateStream.value.isAddingNewContact)
                viewModelScope.launch { contactRepository.addContact(screenStateStream.value.contact) }
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

    fun updateCategory(category: ContactCategory) {
        val newCategories = if (_screenStateStream.value.contact.categories.contains(category))
            _screenStateStream.value.contact.categories - category
        else
            _screenStateStream.value.contact.categories + category

        updateContactState(_screenStateStream.value.contact.copy(categories = newCategories))
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

    fun showLastContactedDatePicker(isShowing: Boolean) {
        _screenStateStream.value = _screenStateStream.value.copy(isShowingLastContactedDatePicker = isShowing)
    }

    fun showAddCategoryModal(isShowing: Boolean) {
        _screenStateStream.value = _screenStateStream.value.copy(isShowingAddCategoryModal = isShowing)
    }

    fun updateNewCategoryName(newCategoryName: String) {
        _screenStateStream.value = _screenStateStream.value.copy(newCategoryName = newCategoryName)
    }

    fun addCategory() {
        _screenStateStream.value.newCategoryName?.let {
            val newCategory = ContactCategory.Custom(it)
            if (!_screenStateStream.value.displayedCategories.contains(newCategory)) {
                _screenStateStream.value = _screenStateStream.value.copy(
                    displayedCategories = _screenStateStream.value.displayedCategories + newCategory
                )
            }
        }
    }

}

data class EditContactScreenState(
    val contact: Contact,
    val isShowingLastContactedDatePicker: Boolean = false,
    val isShowingAddCategoryModal: Boolean = false,
    val isAddingNewContact: Boolean = false,
    val displayedCategories: List<ContactCategory> = listOf(ContactCategory.Family, ContactCategory.Friends),
    val newCategoryName: String? = null,
)

data class EditContactValidationState(
    val isNameError: Boolean = false,
    val isCategoryError: Boolean = false,
    val isCountryError: Boolean = false,
    val isContactMethodError: Boolean = false,
    val isNoteError: Boolean = false,
    val isLastContactedError: Boolean = false,
)