package cz.wz.jelinekp.personalrelationshipmanagement.ui.homescreen

import android.app.Application
import androidx.lifecycle.*
import cz.wz.jelinekp.personalrelationshipmanagement.data.network.ContactDb
import cz.wz.jelinekp.personalrelationshipmanagement.domain.model.Contact
import cz.wz.jelinekp.personalrelationshipmanagement.domain.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrmAppViewModel
@Inject constructor(
	private val repository: ContactRepository
) : ViewModel() {
	
	val contacts = repository.getAllContactsFromRoom()
	
	fun addContact(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
		repository.addContactToRoom(contact)
	}
}