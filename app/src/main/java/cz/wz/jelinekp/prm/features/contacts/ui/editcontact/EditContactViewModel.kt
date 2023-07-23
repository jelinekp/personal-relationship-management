package cz.wz.jelinekp.prm.features.contacts.ui.editcontact

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.wz.jelinekp.prm.features.categories.data.CategoryRepository
import cz.wz.jelinekp.prm.features.categories.data.db.ContactWithCategories
import cz.wz.jelinekp.prm.features.categories.model.Category
import cz.wz.jelinekp.prm.features.contacts.data.ContactRepository
import cz.wz.jelinekp.prm.features.contacts.model.Contact
import cz.wz.jelinekp.prm.navigation.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class EditContactViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val contactRepository: ContactRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {
    
    private val _screenStateStream = MutableStateFlow<EditContactScreenState>(
        EditContactScreenState(
            contact = Contact.emptyContact,
        ),
    )
    val screenStateStream get() = _screenStateStream.asStateFlow()

    private val _validationSharedFlowStream = MutableSharedFlow<EditContactValidationState>()
    val validationSharedFlowStream get() = _validationSharedFlowStream.asSharedFlow()
    
    init {
        viewModelScope.launch {
            val contactId: String? = savedStateHandle[Screen.EditContactScreen.ID]
            var contactFlow: Flow<Contact?> = flowOf(Contact.emptyContact)
            val allCategoriesFlow = categoryRepository.getAllCategoryFromRoom()
            var activeCategoriesFlow: Flow<ContactWithCategories> = emptyFlow()
            var contactIdLong : Long = 0
            
            if (contactId == null || contactId == "null" || contactId == "") {
                contactIdLong = contactRepository.addContact(Contact.emptyContact)
                _screenStateStream.update {
                    it.copy(
                        isAddingNewContact = true,
                        allCategories = allCategoriesFlow.first()
                    )
                }
            } else {
                contactIdLong = contactId.toLong()
            }
            contactFlow = contactRepository.getContactById(contactIdLong)
            activeCategoriesFlow = categoryRepository.getCategoriesOfContact(contactIdLong)
            
            combine(contactFlow, allCategoriesFlow, activeCategoriesFlow) { contact, allCategories, activeCategories ->
                Log.d("Category repository", "update - $allCategories")
                _screenStateStream.value.copy (
                    contact = contact ?: Contact.emptyContact,
                    allCategories = allCategories,
                    activeCategories = activeCategories.categories,
                )
            }.collectLatest { newScreenState ->
                _screenStateStream.update { _ ->
                    newScreenState
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
        val contactId = _screenStateStream.value.contact.id
        
        if (validateInputs()) {
            val deleteCoroutine = viewModelScope.launch {
                _screenStateStream.value.allCategories.forEach {
                    categoryRepository.deleteContactCategory(it, contactId)
                }
            }
            
            viewModelScope.launch {
                deleteCoroutine.join() // waiting for the delete to finish before inserting again
                
                contactRepository.updateContact(_screenStateStream.value.contact).also {
                    _screenStateStream.value.activeCategories.forEach {
                        categoryRepository.insertContactCategory(it, contactId)
                    }
                }
            }
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

    fun updateActiveCategories(category: Category) {
        val contactId = screenStateStream.value.contact.id
        
        viewModelScope.launch {
            if (_screenStateStream.value.activeCategories.contains(category))
                _screenStateStream.update {
                    _screenStateStream.value.copy(activeCategories = _screenStateStream.value.activeCategories - category)
                }
            //categoryRepository.deleteContactCategory(category, contactId)
            else
                _screenStateStream.update {
                    _screenStateStream.value.copy(activeCategories = _screenStateStream.value.activeCategories + category)
                }
            //categoryRepository.insertContactCategory(category, contactId)
        }
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
        updateContactState(_screenStateStream.value.contact.copy(lastContacted = lastContacted.plusDays(1)))
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
        
        viewModelScope.launch {
            _validationSharedFlowStream.emit(EditContactValidationState(isCategoryError = false))
        }
        _screenStateStream.value.newCategoryName?.let {
            if (it.length in 1..30) {
                val newCategory = Category(it)
                if (!_screenStateStream.value.activeCategories.contains(newCategory)) {
                    viewModelScope.launch { categoryRepository.insertCategory(newCategory) }
                    _screenStateStream.value = _screenStateStream.value.copy(
                        allCategories = _screenStateStream.value.allCategories + newCategory,
                        activeCategories = _screenStateStream.value.activeCategories + newCategory
                    )
                } else {
                
                }
            } else {
                viewModelScope.launch {
                    _validationSharedFlowStream.emit(EditContactValidationState(isCategoryError = true))
                }
            }
        }
    }
    
    fun revertChanges() {
        if (_screenStateStream.value.isAddingNewContact) {
            viewModelScope.launch {
                contactRepository.deleteContact(_screenStateStream.value.contact.id)
            }
        }
    }
    
}

data class EditContactScreenState(
    val contact: Contact,
    val isShowingLastContactedDatePicker: Boolean = false,
    val isShowingAddCategoryModal: Boolean = false,
    val isAddingNewContact: Boolean = false,
    val activeCategories: List<Category> = emptyList(),
    val allCategories: List<Category> = emptyList(),
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