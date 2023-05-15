package cz.wz.jelinekp.prm.features.contacts.ui.list

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import cz.wz.jelinekp.prm.R
import cz.wz.jelinekp.prm.features.contacts.domain.Contact
import cz.wz.jelinekp.prm.features.contacts.ui.components.PrmTopBar
import org.koin.androidx.compose.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
	onNavigateToAddContact: () -> Unit,
	listViewModel: ContactListViewModel = koinViewModel(),
	modifier: Modifier = Modifier
) {
	val screenState by listViewModel.screenStateStream.collectAsStateWithLifecycle()
	
	Scaffold(
		topBar = { PrmTopBar(stringResource(R.string.app_name)) },
		floatingActionButtonPosition = FabPosition.End,
		floatingActionButton = { AddContactFab (onClick = onNavigateToAddContact) },
	) { paddingValues ->
		Column(
			modifier = Modifier
				.padding(paddingValues)
				.fillMaxSize()
		) {

			when (screenState) {
				is ContactListScreenState.Loading -> LoadingState()
				is ContactListScreenState.Loaded -> LoadedState(
					(screenState as ContactListScreenState.Loaded).contacts.collectAsStateWithLifecycle(
					initialValue = emptyList()
				).value)
			}
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
	contacts: List<Contact>
) {
	Text(
		text = stringResource(id = R.string.who_to_contact),
		style = MaterialTheme.typography.headlineSmall,
		color = MaterialTheme.colorScheme.onBackground,
		modifier = Modifier
			.padding(8.dp)
	)
	LazyColumn(
		modifier = Modifier
			.background(
				MaterialTheme.colorScheme.background
			)
			.padding(horizontal = 16.dp)
			.fillMaxSize(),
		verticalArrangement = Arrangement.spacedBy(12.dp),
		contentPadding = PaddingValues(bottom = 192.dp)
	) {
		items(contacts, key = { it.id }) {contact ->
			ContactItem(
				contact = contact)
		}
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactItem(
	//viewModel: PrmAppViewModel,
    contact: Contact,
    modifier: Modifier = Modifier
) {
	var expanded by remember {
		mutableStateOf(false)
	}
	
	val color by animateColorAsState(targetValue =
		if (expanded)
			MaterialTheme.colorScheme.primaryContainer
		else
			MaterialTheme.colorScheme.secondaryContainer)
	
	Card(
		modifier = modifier
			.fillMaxWidth(),
		elevation = cardElevation(4.dp),
		onClick = { expanded = !expanded }
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
					.weight(9f)
					.padding(8.dp)
			) {
				BasicContactInfo(contact = contact)
				if (expanded) {
					MoreContactInfo(contact = contact)
				}
			}
			Column(
				modifier = Modifier
					.weight(7f)
					.padding(4.dp),
				verticalArrangement = Arrangement.spacedBy(6.dp),
				horizontalAlignment = Alignment.End
			) {
				ContactedTodayButton()
				if (expanded) {
					SetContactedDateButton()
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
		if (contact.country != null) {
			Text(
				text = stringResource(id = R.string.country, contact.country),
				style = MaterialTheme.typography.bodyMedium,
			)
		}
		if (contact.contactMethod != null) {
			Text(
				text = stringResource(id = R.string.contactMethod, contact.contactMethod),
				style = MaterialTheme.typography.bodyMedium,
			)
		}
		if (contact.note != null) {
			Text(
				text = stringResource(id = R.string.note, contact.note),
				style = MaterialTheme.typography.bodyMedium,
			)
		}
	}
}

@Composable
fun ContactedTodayButton(
	modifier: Modifier = Modifier
) {
	Button(
		onClick = { /*TODO*/ },
		modifier = modifier
	) {
		Text(
			text = stringResource(id = R.string.contacted_today),
			style = MaterialTheme.typography.bodySmall
		)
	}
}

@Composable
fun SetContactedDateButton(
	modifier: Modifier = Modifier
) {
	Button(
		onClick = { /*TODO*/ },
		modifier = modifier
	) {
		Text(
			text = stringResource(id = R.string.set_contacted_date),
			style = MaterialTheme.typography.bodySmall
		)
	}
}

@Composable
fun AddContactFab(
	onClick: () -> Unit
) {
	FloatingActionButton(onClick = onClick){
		Text(
			text = "+",
			fontSize = 36.sp,
			fontWeight = FontWeight.Light
		)
	}
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
	ContactsScreen(
		{}
	)
}