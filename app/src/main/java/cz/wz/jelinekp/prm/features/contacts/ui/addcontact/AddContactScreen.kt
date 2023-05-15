@file:OptIn(ExperimentalMaterial3Api::class)

package cz.wz.jelinekp.prm.features.contacts.ui.addcontact

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.wz.jelinekp.prm.features.contacts.domain.Contact
import cz.wz.jelinekp.prm.features.contacts.ui.components.PrmTopBar
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactScreen(
	viewModel: AddContactViewModel = koinViewModel()
) {
	val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()

	var name: String
	var country: String
	var contactMethod: String
	var note: String
	
	Scaffold(
		topBar = {
			PrmTopBar(topBarText = "Add Contact")
		},
		content = { paddingValues ->
			//Log.d("Padding values:", "$paddingValues")
			Column(
				modifier = Modifier
					.padding(paddingValues)
					.padding(horizontal = 10.dp)
					.fillMaxSize()
			) {
				AddContactTextField(
					label = "Name",
					inputText = screenState.name,
					onValueChange = { text -> viewModel.updateState(screenState.copy(name = text)) }
				)
				AddContactTextField(
					label = "Country",
					inputText = screenState.country ?: "",
					onValueChange = { text -> viewModel.updateState(screenState.copy(country = text)) }
				)
				AddContactTextField(
					label = "Contact method",
					inputText = screenState.contactMethod ?: "",
					onValueChange = { text -> viewModel.updateState(screenState.copy(contactMethod = text)) }
				)
				AddContactTextField(
					label = "Note",
					inputText = screenState.note ?: "",
					onValueChange = { text -> viewModel.updateState(screenState.copy(note = text)) }
				)
				
				Log.d("Contact information:", "name: ${screenState.name}, country: ${screenState.country}, contact method: ${screenState.contactMethod}, note: ${screenState.note}")
			}
		},
		bottomBar = {
			BottomAppBar(
				containerColor = MaterialTheme.colorScheme.primary
			) {
				Button(
					onClick = { viewModel.addContact() },
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 16.dp)
				) {
					Text("Add Contact")
				}
			}
		}
	)
}

@Composable
fun AddContactTextField(
	label: String = "",
	inputText: String = "",
	onValueChange: (text: String) -> Unit,
) {
	OutlinedTextField(
		value = inputText,
		label = { Text(text = label) },
		onValueChange = onValueChange,
		modifier = Modifier.fillMaxWidth()
	)
	// Log.d("Input text", "$label: ${inputText.text}")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
	AddContactScreen()
}