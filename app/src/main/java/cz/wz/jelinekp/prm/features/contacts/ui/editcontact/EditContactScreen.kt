package cz.wz.jelinekp.prm.features.contacts.ui.editcontact

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.wz.jelinekp.prm.R
import cz.wz.jelinekp.prm.features.categories.draganddrop.DragTarget
import cz.wz.jelinekp.prm.features.categories.draganddrop.DragableScreen
import cz.wz.jelinekp.prm.features.categories.draganddrop.DropItem
import cz.wz.jelinekp.prm.features.categories.model.Category
import cz.wz.jelinekp.prm.features.contacts.ui.components.LastContactedDatePicker
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun EditContactScreen(
    navigateUp: () -> Unit,
    viewModel: EditContactViewModel = koinViewModel()
) {
    val screenState by viewModel.screenStateStream.collectAsStateWithLifecycle()
    val validationFlow by viewModel.validationSharedFlowStream.collectAsStateWithLifecycle(
        initialValue = EditContactValidationState()
    )
    val screenWidth = LocalConfiguration.current.screenWidthDp

    BackHandler {
        if (screenState.isAnythingChanged)
            viewModel.showDiscardChangesModal(true)
        else
            navigateUp()
    }

    EditContactValidationToasts(validationFlow = validationFlow)

    Scaffold(
        topBar = { EditContactTopBar(
            screenState = screenState,
            closeDiscardModal = { viewModel.showDiscardChangesModal(false) },
            applyChanges = { viewModel.applyChanges() },
            navigateUp = navigateUp,
        ) },
        content = { paddingValues ->
            EditContactScreenContent(
                paddingValues = paddingValues,
                screenState = screenState,
                viewModel = viewModel,
                validationFlow = validationFlow,
                screenWidth = screenWidth,
                navigateUp = navigateUp,
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactTopBar(
    screenState: EditContactScreenState,
    closeDiscardModal: () -> Unit,
    applyChanges: () -> Boolean,
    navigateUp: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(
                    if (screenState.isAddingNewContact) R.string.add_contact else
                        R.string.edit_contact_screen_name
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                if (screenState.isAnythingChanged)
                    closeDiscardModal()
                else
                    navigateUp()
            }) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = stringResource(R.string.back_icon)
                )
            }
        },
        actions = {
            Button(
                modifier = Modifier.padding(end = 4.dp),
                onClick = {
                    if (applyChanges()) {
                        navigateUp()
                    }
                },
            ) {
                Text(stringResource(R.string.save))
            }
        },
    )
}

@Composable
fun EditContactScreenContent(
   paddingValues: PaddingValues,
   screenState: EditContactScreenState,
   viewModel: EditContactViewModel,
   validationFlow: EditContactValidationState,
   screenWidth: Int,
   navigateUp: () -> Unit,
) {
    val nameFocusRequester = remember { FocusRequester() }
    val countryFocusRequester = remember { FocusRequester() }
    val contactMethodFocusRequester = remember { FocusRequester() }
    val noteFocusRequester = remember { FocusRequester() }
    val lastContactedFocusRequester = remember { FocusRequester() }

    DragableScreen {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .verticalScroll(state = ScrollState(0)),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ContactTextField(
                label = stringResource(id = R.string.name),
                inputText = screenState.contact.name,
                onValueChange = { text -> viewModel.updateName(name = text) },
                isError = validationFlow.isNameError,
                keyboardActions = KeyboardActions(onNext = { countryFocusRequester.requestFocus() }),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Words
                ),
                modifier = Modifier.focusRequester(nameFocusRequester)
            )
            ContactTextField(
                label = stringResource(id = R.string.country),
                inputText = screenState.contact.country ?: "",
                onValueChange = { text -> viewModel.updateCountry(country = text) },
                isError = validationFlow.isCountryError,
                keyboardActions = KeyboardActions(onNext = { contactMethodFocusRequester.requestFocus() }),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Words
                ),
                modifier = Modifier.focusRequester(countryFocusRequester)
            )
            ContactTextField(
                label = stringResource(R.string.contact_method),
                inputText = screenState.contact.contactMethod ?: "",
                onValueChange = { text -> viewModel.updateContactMethod(contactMethod = text) },
                isError = validationFlow.isContactMethodError,
                keyboardActions = KeyboardActions(onNext = { noteFocusRequester.requestFocus() }),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                modifier = Modifier.focusRequester(contactMethodFocusRequester)
            )
            ContactTextField(
                label = stringResource(id = R.string.note),
                inputText = screenState.contact.note ?: "",
                onValueChange = { text -> viewModel.updateNote(note = text) },
                singleLine = false,
                isError = validationFlow.isNoteError,
                keyboardActions = KeyboardActions(onNext = {
                    lastContactedFocusRequester.requestFocus()
                    viewModel.showLastContactedDatePicker(isShowing = true)
                }),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                modifier = Modifier.focusRequester(noteFocusRequester)
            )

            ReadonlyTextField(
                value = screenState.contact.lastContacted.format(
                    DateTimeFormatter.ofPattern(
                        stringResource(R.string.date_format_to_display)
                    )
                )
                    .toString(),
                onValueChange = {},
                onClick = { viewModel.showLastContactedDatePicker(isShowing = true) },
                label = stringResource(R.string.last_contacted_date),
                isError = validationFlow.isLastContactedError,
                modifier = Modifier.focusRequester(lastContactedFocusRequester),
            )

            EditContactCategories(
                viewModel = viewModel,
                screenState = screenState,
            )
        }

        EditContactDragAndDrop(
            viewModel = viewModel,
            screenState = screenState,
            screenWidth = screenWidth,
        )

        EditContactModals(
            screenState = screenState,
            viewModel = viewModel,
            navigateUp = navigateUp,
        )
    }
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
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
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
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(R.string.dropdown)
                )
            },
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable(onClick = onClick),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditContactCategories(
    viewModel: EditContactViewModel,
    screenState: EditContactScreenState,
) {
    Text(text = stringResource(R.string.select_categories))
    FlowRow() {
        screenState.allCategories.forEach { category ->
            DragTarget(dataToDrop = category, viewModel = viewModel) {
                FilterChip(
                    selected = screenState.activeCategories.contains(category),
                    onClick = { viewModel.updateActiveCategories(category) },
                    label = {
                        Text(
                            text = category.categoryName
                        )
                    },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
        AssistChip(
            onClick = {
                viewModel.updateNewCategoryName("")
                viewModel.showAddCategoryModal(true)
            },
            label = { Text(text = stringResource(R.string.add_category)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
        )
    }
}

@Composable
fun EditContactDragAndDrop(
    viewModel: EditContactViewModel,
    screenState: EditContactScreenState,
    screenWidth: Int,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = screenWidth.dp / 2.5f)
            .width(screenWidth.dp / 2)
            .height(110.dp)
            .absoluteOffset(y = Dp(LocalConfiguration.current.screenHeightDp.toFloat() - 110.dp.value))
        //.align(Alignment.BottomCenter)
        ,verticalArrangement = Arrangement.Bottom,
    ) {
        AnimatedVisibility(
            visible = screenState.draggedCategory != null,
            enter = slideInVertically(initialOffsetY = { it }),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            DropItem<Category>(
                modifier = Modifier
                    .fillMaxSize(),
            ) { isInBound, categoryItem ->
                if (categoryItem != null) {
                    LaunchedEffect(key1 = categoryItem) {
                        viewModel.setChipDragged(categoryItem)
                    }
                }

                val deleteColor = if (isInBound)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onBackground


                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.BottomCenter)
                    // .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(R.string.drag_here_to_delete_category),
                        tint = deleteColor,
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.BottomCenter)
                            .padding(top = 24.dp)
                    )
                }

                if (isInBound) {
                    viewModel.isInBound(true)
                } else {
                    viewModel.isInBound(false)
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactModals(
    screenState: EditContactScreenState,
    viewModel: EditContactViewModel,
    navigateUp: () -> Unit,
) {
    if (screenState.isShowingLastContactedDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                viewModel.updateLastContacted(LocalDateTime.now())
                viewModel.showLastContactedDatePicker(isShowing = false)
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.showLastContactedDatePicker(
                        isShowing = false
                    )
                }) {
                    Text(text = stringResource(id = R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.updateLastContacted(LocalDateTime.now())
                    viewModel.showLastContactedDatePicker(isShowing = false)
                }) {
                    Text(text = stringResource(id = R.string.cancel))
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

    if (screenState.isShowingDeleteCategoryModal) {
        AlertDialog(
            onDismissRequest = { viewModel.abortDeletionOfCategory() },
            title = {
                Text(
                    text = stringResource(
                        id = R.string.delete_category,
                        screenState.draggedCategory?.categoryName
                            ?: ""
                    )
                )
            },
            dismissButton = {
                TextButton(onClick = { viewModel.abortDeletionOfCategory() }) {
                    Text(text = stringResource(R.string.dismiss))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteCategory()
                }) {
                    Text(text = stringResource(id = R.string.delete_cat))
                }
            },
            text = {},

            )
    }

    if (screenState.isShowingAddCategoryModal) {
        AlertDialog(
            onDismissRequest = { viewModel.showAddCategoryModal(false) },
            title = { Text(stringResource(id = R.string.add_category)) },
            dismissButton = {
                TextButton(onClick = { viewModel.showAddCategoryModal(false) }) {
                    Text(text = stringResource(R.string.dismiss))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.addCategory()
                    viewModel.showAddCategoryModal(false)
                }) {
                    Text(text = stringResource(id = R.string.add))
                }
            },
            text = {
                ContactTextField(
                    inputText = screenState.newCategoryName ?: "",
                    onValueChange = { viewModel.updateNewCategoryName(it) },
                    label = stringResource(R.string.category_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        viewModel.addCategory()
                        viewModel.showAddCategoryModal(false)
                    })
                )
            },
        )
    }

    if (screenState.isShowingDiscardChangesModal) {
        AlertDialog(
            onDismissRequest = {
                viewModel.showDiscardChangesModal(false)
                               },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.showDiscardChangesModal(false)
                    viewModel.revertChanges()
                    navigateUp()
                }) {
                    Text(text = stringResource(R.string.discard))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.showDiscardChangesModal(false)
                    if (viewModel.applyChanges()) {
                        navigateUp()
                    }
                }) {
                    Text(text = stringResource(id = R.string.save))
                }
            },
            text = {
                Text(text = "Your changes have not been saved")
            },
        )
    }
}

@Composable
fun EditContactValidationToasts(
    validationFlow: EditContactValidationState,
) {
    val context = LocalContext.current

    if (validationFlow.isLastContactedError)
        Toast.makeText(
            context,
            stringResource(R.string.last_contacted_date_can_t_be_in_the_future),
            Toast.LENGTH_SHORT
        )
            .show()
    else if (validationFlow.isNameError)
        Toast.makeText(
            context,
            stringResource(R.string.name_field_can_t_be_empty),
            Toast.LENGTH_SHORT
        ).show()
    else if (validationFlow.isCategoryError)
        Toast.makeText(context, stringResource(R.string.wrong_category), Toast.LENGTH_SHORT).show()
}
