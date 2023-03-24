@file:OptIn(ExperimentalMaterial3Api::class)

package cz.wz.jelinekp.personalrelationshipmanagement.ui.addcontact

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
import cz.wz.jelinekp.personalrelationshipmanagement.domain.model.Contact
import cz.wz.jelinekp.personalrelationshipmanagement.ui.components.PrmTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactScreen() {
	var name: String
	var country: String
	var contactMethod: String
	var note: String
	
	Scaffold(
		topBar = {
			PrmTopBar(topBarText = "Add Contact")
		},
		content = { paddingValues ->
			Log.d("Padding values:", "$paddingValues")
			Column(
				modifier = Modifier
					.padding(paddingValues)
					.padding(horizontal = 10.dp)
					.fillMaxSize()
			) {
				name = prmTextField(
					label = "Name"
				)
				country = prmTextField(
					label = "Country"
				)
				contactMethod = prmTextField(
					label = "Contact method"
				)
				note = prmTextField(
					label = "Note"
				)
				
				Log.d("Contact information:", "name: $name, country: $country, contact method: $contactMethod, note: $note")
			}
		},
		bottomBar = {
			BottomAppBar(
				containerColor = MaterialTheme.colorScheme.primary
			) {
				Button(
					onClick = { /* Handle add contact button click */ },
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
fun prmTextField(
	label: String = "",
	
): String {
	var inputText by remember { mutableStateOf(TextFieldValue("")) }
	OutlinedTextField(
		value = inputText,
		label = { Text(text = label) },
		onValueChange = { inputText = it },
		modifier = Modifier.fillMaxWidth()
	)
	// Log.d("Input text", "$label: ${inputText.text}")
	return inputText.text
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
	AddContactScreen()
}