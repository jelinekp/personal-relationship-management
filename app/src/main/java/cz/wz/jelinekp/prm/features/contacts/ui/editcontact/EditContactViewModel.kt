package cz.wz.jelinekp.prm.features.contacts.ui.editcontact

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.wz.jelinekp.prm.features.categories.data.CategoryRepository
import cz.wz.jelinekp.prm.features.categories.model.Category
import cz.wz.jelinekp.prm.features.contacts.data.ContactRepository
import cz.wz.jelinekp.prm.features.contacts.model.Contact
import cz.wz.jelinekp.prm.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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
            loadContactData()
        }
    }
    
    private suspend fun loadContactData() {
        val contactId: String? = savedStateHandle[Screen.EditContactScreen.ID]
        val contactIdLong: Long

        if (contactId == "addingNewContact" || contactId == null || contactId == "") {
            contactIdLong = contactRepository.addContact(Contact.emptyContact)
            _screenStateStream.update {
                it.copy(
                    isAddingNewContact = true,
                )
            }
        } else {
            contactIdLong = contactId.toLong()
        }
        val contactFlow = contactRepository.getContactById(contactIdLong)
        val activeCategoriesFlow =
            categoryRepository.getCategoriesOfContact(contactIdLong)
        
        viewModelScope.launch {
            val allCategoriesFlow = categoryRepository.getAllCategoryFromRoom()
            allCategoriesFlow.collectLatest { allCategoriesList ->
                _screenStateStream.update {
                    it.copy(
                        allCategories = allCategoriesList
                    )
                }
            }
        }
        
        viewModelScope.launch {
            contactFlow.collectLatest { contact ->
                _screenStateStream.update {
                    it.copy(contact = contact ?: Contact.emptyContact)
                }
            }
        }
        
        viewModelScope.launch {
            activeCategoriesFlow.collectLatest { activeCategories ->
                _screenStateStream.update {
                    it.copy(activeCategories = activeCategories.categories)
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
            // We're deleting all the contact - category connections
            val deleteCoroutine = viewModelScope.launch {
                _screenStateStream.value.allCategories.forEach {
                    categoryRepository.deleteContactCategory(it, contactId)
                }
            }
            
            viewModelScope.launch {
                deleteCoroutine.join() // waiting for the delete to finish before inserting again
                
                // and than adding only the active connections
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
        
        viewModelScope.launch {
            _validationSharedFlowStream.emit(EditContactValidationState(isCategoryError = false))
        }
        _screenStateStream.value.newCategoryName?.let {
            if (it.length in 1..30) {
                val newCategory = Category(it)
                if (!_screenStateStream.value.allCategories.contains(newCategory)) {
                    _screenStateStream.value = _screenStateStream.value.copy(
                        allCategories = _screenStateStream.value.allCategories + newCategory,
                        activeCategories = _screenStateStream.value.activeCategories + newCategory
                    )
                    viewModelScope.launch { categoryRepository.insertCategory(newCategory) }
                } else {
                
                }
            } else {
                viewModelScope.launch {
                    _validationSharedFlowStream.emit(EditContactValidationState(isCategoryError = true))
                }
            }
        }
    }
    
    fun setChipDragged(category: Category) {
        _screenStateStream.update { it.copy(draggedCategory = category) }
    }
    
    fun dragToDelete() {
        if (_screenStateStream.value.isInDeleteBound)
            _screenStateStream.update {
                it.copy(
                    isShowingDeleteCategoryModal = true
                )
            }
    }
    
    fun abortDeletionOfCategory() {
        _screenStateStream.update {
            it.copy(isShowingDeleteCategoryModal = false)
        }
        stopChipDragging()
    }
    
    fun deleteCategory() {
        viewModelScope.launch {
            _screenStateStream.value.draggedCategory?.let { categoryRepository.deleteCategory(it) }
            _screenStateStream.update {
                it.copy(
                    draggedCategory = null,
                    isShowingDeleteCategoryModal = false
                )
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
    
    fun stopChipDragging() {
        _screenStateStream.update {
            it.copy(draggedCategory = null)
        }
    }

    fun isInBound(bound: Boolean) {
        _screenStateStream.update {
            it.copy(isInDeleteBound = bound)
        }
    }

    companion object {
        const val TAG = "EditContactVM"
    }
    
}

data class EditContactScreenState(
    val contact: Contact,
    val isShowingLastContactedDatePicker: Boolean = false,
    val isShowingAddCategoryModal: Boolean = false,
    val isShowingDeleteCategoryModal: Boolean = false,
    val isAddingNewContact: Boolean = false,
    val isInDeleteBound: Boolean = false,
    val activeCategories: List<Category> = emptyList(),
    val allCategories: List<Category> = emptyList(),
    val newCategoryName: String? = null,
    val draggedCategory: Category? = null,
)

data class EditContactValidationState(
    val isNameError: Boolean = false,
    val isCategoryError: Boolean = false,
    val isCountryError: Boolean = false,
    val isContactMethodError: Boolean = false,
    val isNoteError: Boolean = false,
    val isLastContactedError: Boolean = false,
)