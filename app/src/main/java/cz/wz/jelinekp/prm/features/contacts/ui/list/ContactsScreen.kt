package cz.wz.jelinekp.prm.features.contacts.ui.list

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.wz.jelinekp.prm.R
import cz.wz.jelinekp.prm.features.contacts.domain.Contact
import cz.wz.jelinekp.prm.features.contacts.ui.components.PrmTopBar
import org.koin.androidx.compose.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.LocalDateTime

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
				).value,
					onContactedTodayClick = listViewModel::updateLastContacted
				)
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
	contacts: List<Contact>,
	onContactedTodayClick: (contactId: Long) -> Unit
) {
	/*Text(
		text = stringResource(id = R.string.who_to_contact),
		style = MaterialTheme.typography.bodyLarge,
		color = MaterialTheme.colorScheme.onBackground,
		modifier = Modifier
			.padding(8.dp).padding(start = 8.dp)
	)*/
	//Divider()
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
		items(contacts, key = { it.id }) {contact ->
			ContactItem(
				contact = contact,
				onContactedTodayClick = onContactedTodayClick
			)
		}
	}
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContactItem(
	//viewModel: PrmAppViewModel,
    contact: Contact,
    modifier: Modifier = Modifier,
	onContactedTodayClick: (contactId: Long) -> Unit
) {
	var expanded by remember {
		mutableStateOf(false)
	}

	val color by animateColorAsState(targetValue =
		if (expanded)
			MaterialTheme.colorScheme.primaryContainer
		else
			MaterialTheme.colorScheme.secondaryContainer)

	val context = LocalContext.current

	Card(
		modifier = modifier
			.fillMaxWidth()
			.combinedClickable(
				onLongClick = {
					onContactedTodayClick(contact.id)
					Toast.makeText(context, "${contact.name} contacted today", Toast.LENGTH_SHORT).show()
				},
				onClick = { expanded = !expanded }
			)
			,
		elevation = cardElevation(4.dp),
		//onClick = { expanded = !expanded },
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
				ContactCardButton(
					text = stringResource(id = R.string.set_contacted_date),
					onClick = {
						Toast.makeText(context, "Set contacted Date", Toast.LENGTH_SHORT).show()
					}
				)
				if (expanded) {
					ContactCardButton(
						text = stringResource(id = R.string.contacted_today),
						onClick = { onContactedTodayClick(contact.id) }
					)
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
fun ContactCardButton(
	onClick: () -> Unit,
	text: String,
	modifier: Modifier = Modifier
) {
	Button(
		onClick = onClick,
		modifier = modifier
	) {
		Text(
			text = text,
			style = MaterialTheme.typography.bodySmall
		)
	}
}

@Composable
fun AddContactFab(
	onClick: () -> Unit
) {
	FloatingActionButton(
		onClick = onClick,
		containerColor = MaterialTheme.colorScheme.primary,
		contentColor = MaterialTheme.colorScheme.onPrimary
	){
		Icon(Icons.Default.Add, "", Modifier.size(32.dp))
	}
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
	ContactsScreen(
		{}
	)
}