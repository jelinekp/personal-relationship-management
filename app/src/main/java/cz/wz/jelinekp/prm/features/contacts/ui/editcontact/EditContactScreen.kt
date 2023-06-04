package cz.wz.jelinekp.prm.features.contacts.ui.editcontact

import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.wz.jelinekp.prm.features.contacts.ui.components.LastContactedDatePicker
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactScreen(
    navigateUp: () -> Unit,
    viewModel: EditContactViewModel = koinViewModel()
) {
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()
    val validationFlow by viewModel.validationSharedFlowStream.collectAsStateWithLifecycle(
        initialValue = EditContactValidationState()
    )

    val nameFocusRequester = remember { FocusRequester() }
    val countryFocusRequester = remember { FocusRequester() }
    val contactMethodFocusRequester = remember { FocusRequester() }
    val noteFocusRequester = remember { FocusRequester() }
    val lastContactedFocusRequester = remember { FocusRequester() }

    val context = LocalContext.current

    if (validationFlow.lastContactedError) {
        Toast.makeText(context, "Last contacted date can't be in the future", Toast.LENGTH_SHORT).show()
    } else if (validationFlow.nameError) {
        Toast.makeText(context, "Name field can't be empty", Toast.LENGTH_SHORT).show()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (screenState.isAddingNewContact) "Add Contact" else "Edit contact",
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Back icon"
                        )
                    }
                },
                actions = {
                    Button(
                        modifier = Modifier.padding(end = 4.dp),
                        onClick = { if (viewModel.applyChanges()) {
                            navigateUp()
                        } },
                    ) {
                        Text("Save")
                    }
                },
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 10.dp)
                    .fillMaxSize()
                    .verticalScroll(state = ScrollState(0)),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ContactTextField(
                    label = "Name",
                    inputText = screenState.contact.name,
                    onValueChange = { text -> viewModel.updateName(name = text) },
                    isError = validationFlow.nameError,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { countryFocusRequester.requestFocus() }),
                    modifier = Modifier.focusRequester(nameFocusRequester)
                )
                ContactTextField(
                    label = "Country",
                    inputText = screenState.contact.country ?: "",
                    onValueChange = { text -> viewModel.updateCountry(country = text) },
                    isError = validationFlow.countryError,
                    keyboardActions = KeyboardActions(onNext = { contactMethodFocusRequester.requestFocus() }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.focusRequester(countryFocusRequester)
                )
                ContactTextField(
                    label = "Contact method",
                    inputText = screenState.contact.contactMethod ?: "",
                    onValueChange = { text -> viewModel.updateContactMethod(contactMethod = text) },
                    isError = validationFlow.contactMethodError,
                    keyboardActions = KeyboardActions(onNext = { noteFocusRequester.requestFocus() }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.focusRequester(contactMethodFocusRequester)
                )
                ContactTextField(
                    label = "Note",
                    inputText = screenState.contact.note ?: "",
                    onValueChange = { text -> viewModel.updateNote(note = text) },
                    singleLine = false,
                    isError = validationFlow.noteError,
                    keyboardActions = KeyboardActions(onNext = {
                        lastContactedFocusRequester.requestFocus()
                        viewModel.showLastContactedDatePicker()
                    }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.focusRequester(noteFocusRequester)
                )

                ReadonlyTextField(
                    value = screenState.contact.lastContacted.format(DateTimeFormatter.ofPattern("d. L. yyyy")).toString(),
                    onValueChange = {},
                    onClick = { viewModel.showLastContactedDatePicker() },
                    label = "Last contacted date",
                    isError = validationFlow.lastContactedError,
                    modifier = Modifier.focusRequester(lastContactedFocusRequester),
                )


                if (screenState.showLastContactedDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = {
                            viewModel.updateLastContacted(LocalDateTime.now())
                            viewModel.hideLastContactedDatePicker()
                        },
                        confirmButton = {
                            TextButton(onClick = { viewModel.hideLastContactedDatePicker() }) {
                                Text(text = "OK")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                viewModel.updateLastContacted(LocalDateTime.now())
                                viewModel.hideLastContactedDatePicker() }) {
                                Text(text = "Cancel")
                            }
                        }
                    ) {
                        LastContactedDatePicker(
                            initialDateTime = LocalDateTime.now(),
                            onValueChange = { lastContacted ->
                                viewModel.updateLastContacted(
                                    lastContacted = lastContacted
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun ContactTextField(
    onValueChange: (text: String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    label: String = "",
    inputText: String = "",
    singleLine: Boolean = true,
    isError: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = inputText,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = modifier.fillMaxWidth(),
        isError = isError,
        singleLine = singleLine,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions.copy(capitalization = KeyboardCapitalization.Words),
        trailingIcon = trailingIcon
    )
}

@Composable
fun ReadonlyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
    label: String,
    isError: Boolean,
    modifier: Modifier = Modifier,
) {
    Box {
        ContactTextField(
            label = label,
            inputText = value,
            onValueChange = onValueChange,
            isError = isError,
            modifier = modifier,
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown") }
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable(onClick = onClick),
        )
    }
}