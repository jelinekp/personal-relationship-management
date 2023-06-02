@file:OptIn(ExperimentalMaterial3Api::class)

package cz.wz.jelinekp.prm.features.contacts.ui.addcontact

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddContactScreen(
    viewModel: AddContactViewModel = koinViewModel()
) {
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()

    val nameFocusRequester = remember { FocusRequester() }
    val countryFocusRequester = remember { FocusRequester() }
    val contactMethodFocusRequester = remember { FocusRequester() }
    val noteFocusRequester = remember { FocusRequester() }
    val lastContactedFocusRequester = remember { FocusRequester() }

    val context = LocalContext.current

    if (screenState.lastContactedError) {
        Toast.makeText(context, "Last contacted date can't be in the future", Toast.LENGTH_SHORT).show()
    } else if (screenState.nameError) {
        Toast.makeText(context, "Name field can't be empty", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Contact",
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO navigate back*/ }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Back icon"
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = { viewModel.addContact() },
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
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AddContactTextField(
                    label = "Name",
                    inputText = screenState.contact.name,
                    onValueChange = { text -> viewModel.updateName(name = text) },
                    isError = screenState.nameError,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { countryFocusRequester.requestFocus() }),
                    modifier = Modifier.focusRequester(nameFocusRequester)
                )
                AddContactTextField(
                    label = "Country",
                    inputText = screenState.contact.country ?: "",
                    onValueChange = { text -> viewModel.updateCountry(country = text) },
                    isError = screenState.countryError,
                    keyboardActions = KeyboardActions(onNext = { contactMethodFocusRequester.requestFocus() }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.focusRequester(countryFocusRequester)
                )
                AddContactTextField(
                    label = "Contact method",
                    inputText = screenState.contact.contactMethod ?: "",
                    onValueChange = { text -> viewModel.updateContactMethod(contactMethod = text) },
                    isError = screenState.contactMethodError,
                    keyboardActions = KeyboardActions(onNext = { noteFocusRequester.requestFocus() }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.focusRequester(contactMethodFocusRequester)
                )
                AddContactTextField(
                    label = "Note",
                    inputText = screenState.contact.note ?: "",
                    onValueChange = { text -> viewModel.updateNote(note = text) },
                    singleLine = false,
                    isError = screenState.noteError,
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
                    isError = screenState.lastContactedError,
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
fun AddContactTextField(
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
        keyboardOptions = keyboardOptions,
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
        AddContactTextField(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LastContactedDatePicker(
    initialDateTime: LocalDateTime,
    onValueChange: (lastContacted: LocalDateTime) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Pre-select a date - converting initialDateTime to milliseconds
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDateTime.toEpochSecond(
                ZoneOffset.UTC
            ) * 1000
        )
        DatePicker(
            state = datePickerState,
            modifier = Modifier,
            )
        val date = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(datePickerState.selectedDateMillis!!),
            ZoneId.systemDefault()
        )
        onValueChange(date)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AddContactScreen()
}