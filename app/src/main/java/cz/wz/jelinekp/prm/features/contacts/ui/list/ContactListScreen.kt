package cz.wz.jelinekp.prm.features.contacts.ui.list

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.wz.jelinekp.prm.R
import cz.wz.jelinekp.prm.features.categories.model.Category
import cz.wz.jelinekp.prm.features.contacts.model.Contact
import cz.wz.jelinekp.prm.features.contacts.ui.components.LastContactedDatePicker
import cz.wz.jelinekp.prm.features.signin.SignInActivity
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    onNavigateToEditContact: (contactId: Long?) -> Unit,
    //onNavigateToCategory: (categoryId: String) -> Unit,
    modifier: Modifier = Modifier,
    listViewModel: ContactListViewModel = koinViewModel()
) {
    val screenState by listViewModel.screenStateStream.collectAsStateWithLifecycle()
    val datePickerState by listViewModel.datePickerScreenStateStream.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            when (screenState) {
                is ContactListScreenState.Loading -> LoadingState()
                is ContactListScreenState.Loaded -> DrawerContent(
                    categories = (screenState as ContactListScreenState.Loaded).categories,
                    onCategoryClick = listViewModel::filterByCategory.also { coroutineScope.launch {
                        drawerState.close()
                    } },
                    selectedCategory = (screenState as ContactListScreenState.Loaded).filteredCategory
                )
            }
        }
    ) {
        Scaffold(
            topBar = { PrmTopBar(
                topBarText = (when (screenState) {
                        is ContactListScreenState.Loaded -> (screenState as ContactListScreenState.Loaded).filteredCategory
                        is ContactListScreenState.Loading -> stringResource(R.string.loading)
                                       }),
                drawerOpen = { coroutineScope.launch { drawerState.open() }
                     })},
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = { AddContactFab(onClick = { onNavigateToEditContact(null) }) },
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                when (screenState) {
                    is ContactListScreenState.Loading -> LoadingState()
                    is ContactListScreenState.Loaded -> LoadedState(
                        contacts = (screenState as ContactListScreenState.Loaded).filteredContacts,
                        activeCategory = (screenState as ContactListScreenState.Loaded).filteredCategory,
                        onContactedTodayClick = listViewModel::updateLastContacted,
                        onDeleteContact = listViewModel::deleteContact,
                        onLastContactedEditClick = listViewModel::showLastContactedDatePicker,
                        onEditContactClick = onNavigateToEditContact
                    )
                }
            }

            var lastContactedVar = LocalDateTime.now()

            if (datePickerState.showLastContactedDatePicker != null) {
                DatePickerDialog(
                    onDismissRequest = {
                        listViewModel.hideLastContactedDatePicker()
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            listViewModel.hideLastContactedDatePicker()
                            listViewModel.updateLastContacted(
                                datePickerState.showLastContactedDatePicker!!,
                                lastContactedVar
                            )
                        }) {
                            Text(text = stringResource(R.string.ok))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            listViewModel.hideLastContactedDatePicker()
                        }) {
                            Text(text = stringResource(R.string.cancel))
                        }
                    }
                ) {
                    LastContactedDatePicker(
                        initialDateTime = LocalDateTime.now(),
                        onValueChange = { lastContacted ->
                            lastContactedVar = lastContacted
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerContent(
    categories: List<Category>,
    onCategoryClick: (categoryId: String) -> Unit,
    selectedCategory: String,
) {
    ModalDrawerSheet {
        Text(stringResource(R.string.app_name), modifier = Modifier.padding(16.dp))
        NavigationDrawerItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(44.dp),
            label = {
                Text(
                    text = stringResource(R.string.all_contacts),
                    style = MaterialTheme.typography.labelLarge,
                ) },
            selected = selectedCategory == "all",
            onClick = { onCategoryClick("all") }
        )
        Divider()
        Text(
            modifier = Modifier.padding(all = 22.dp),
            text = stringResource(R.string.categories),
            style = MaterialTheme.typography.labelMedium,
            )
        categories.forEach { category ->
            NavigationDrawerItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .padding(horizontal = 8.dp),
                label = {
                    Text(
                        text = category.categoryName,
                        style = MaterialTheme.typography.labelLarge
                    ) },
                selected = selectedCategory == category.categoryName,
                onClick = { onCategoryClick(category.categoryName) },
            )
        }
    }
}

@Composable
fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun LoadedState(
    contacts: List<Contact>,
    activeCategory: String,
    onContactedTodayClick: (contactId: Long) -> Unit,
    onDeleteContact: (contactId: Long) -> Unit,
    onLastContactedEditClick: (contactId: Long) -> Unit,
    onEditContactClick: (contactId: Long) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.background
            )
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 192.dp, top = 8.dp)
    ) {
        items(contacts, key = { it.id }) { contact ->
            ContactItem(
                contact = contact,
                onContactedTodayClick = onContactedTodayClick,
                onDeleteContact = onDeleteContact,
                onLastContactedEditClick = onLastContactedEditClick,
                onEditContactClick = onEditContactClick,
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactItem(
    contact: Contact,
    modifier: Modifier = Modifier,
    onContactedTodayClick: (contactId: Long) -> Unit,
    onDeleteContact: (contactId: Long) -> Unit,
    onLastContactedEditClick: (contactId: Long) -> Unit,
    onEditContactClick: (contactId: Long) -> Unit,
) {
    var expanded by rememberSaveable { // TODO extract this to viewModel
        mutableStateOf(false)
    }

    val color by animateColorAsState(
        targetValue =
        if (expanded)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.secondaryContainer, label = ""
    )

    val context = LocalContext.current

    Card(
        modifier = modifier
            .combinedClickable(
                onLongClick = {
                    onContactedTodayClick(contact.id)
                    Toast
                        .makeText(context, "${contact.name} contacted today", Toast.LENGTH_SHORT)
                        .show()
                },
                onClick = { expanded = !expanded }
            )
            .fillMaxWidth(),
        elevation = cardElevation(4.dp),
    ) {
        Row(
            modifier = Modifier
                .background(color)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                BasicContactInfo(contact = contact)
                if (expanded) {
                    MoreContactInfo(contact = contact)
                }
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    modifier = Modifier
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { onLastContactedEditClick(contact.id) }) {
                        Icon(Icons.Default.DateRange, contentDescription = stringResource(R.string.set_last_contacted))
                    }
                }
                if (expanded) {
                    Row(
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { onEditContactClick(contact.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit_contact))
                        }
                        
                        val isDeleteDialogOpen = rememberSaveable {
                            mutableStateOf(false)
                        }
                        
                        if (isDeleteDialogOpen.value) {
                            AlertDialog(
                                onDismissRequest = { isDeleteDialogOpen.value = false },
                                title = { Text(
                                    text = stringResource(
                                        R.string.are_you_sure_to_delete_contact,
                                        contact.name
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                ) },
                                dismissButton = { TextButton(
                                    onClick = { isDeleteDialogOpen.value = false },
                                ) {
                                    Text(stringResource(id = R.string.cancel))
                                } },
                                confirmButton = { TextButton(
                                    onClick = { onDeleteContact(contact.id) },
                                ) {
                                    Text(stringResource(R.string.confirm))
                                } }
                            )
                        }
                        
                        IconButton(onClick = { isDeleteDialogOpen.value = true }) {
                            Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete_contact))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BasicContactInfo(
    contact: Contact
) {
    Text(
        text = contact.name,
        style = MaterialTheme.typography.bodyLarge
    )
    Text(
        text = contact.createdDateFormatted,
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
fun MoreContactInfo(
    contact: Contact
) {
    Column() {
        if (!contact.country.isNullOrBlank()) {
            Text(
                text = stringResource(id = R.string.country_colon_value, contact.country),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        if (!contact.contactMethod.isNullOrBlank()) {
            Text(
                text = stringResource(id = R.string.contactMethod_colon_value, contact.contactMethod),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        if (!contact.note.isNullOrBlank()) {
            Text(
                text = stringResource(id = R.string.note_colon_value, contact.note),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
fun AddContactFab(
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(Icons.Default.Add, null, Modifier.size(30.dp))
        Text(text = "Add contact", modifier = Modifier.padding(start = 4.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrmTopBar(
    topBarText: String,
    drawerOpen: () -> Unit,
) {
    val contextForToast = LocalContext.current

    var dropDownMenuExpanded by remember {
        mutableStateOf(false)
    }
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = drawerOpen) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = stringResource(R.string.menu))
            }
        },
        title = {
            Text(
                text = if (topBarText == "all") stringResource(R.string.all_contacts) else topBarText,
                fontSize = 20.sp
            )
        },
        actions = {
            TopAppBarActionButton(imageVector = Icons.Outlined.MoreVert, description = stringResource(
                            R.string.app_menu)
                        ) {
                dropDownMenuExpanded = true
            }

            DropdownMenu(
                expanded = dropDownMenuExpanded,
                onDismissRequest = {
                    dropDownMenuExpanded = false
                },
                offset = DpOffset(x = 10.dp, y = (-50).dp)
            ) {
                DropdownMenuItem(
                    onClick = {
                        contextForToast.startActivity(
                            Intent(
                                contextForToast,
                                SignInActivity::class.java
                            )
                        )
                        dropDownMenuExpanded = false
                    },
                    text = {
                        Text(
                            text = stringResource(R.string.profile_sync),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    },
                )
            }
        }
    )
}

@Composable
fun TopAppBarActionButton(
    imageVector: ImageVector,
    description: String,
    onClick: () -> Unit
) {
    IconButton(onClick = {
        onClick()
    }) {
        Icon(imageVector = imageVector, contentDescription = description)
    }
}
