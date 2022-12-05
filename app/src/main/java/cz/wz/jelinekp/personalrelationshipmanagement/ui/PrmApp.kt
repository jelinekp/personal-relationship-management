package cz.wz.jelinekp.personalrelationshipmanagement.ui

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.wz.jelinekp.personalrelationshipmanagement.R
import cz.wz.jelinekp.personalrelationshipmanagement.data.Contact
import cz.wz.jelinekp.personalrelationshipmanagement.data.contacts
import cz.wz.jelinekp.personalrelationshipmanagement.data.extractDateForUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrmApp() {
	Scaffold(
		topBar = { PrmTopBar()	},
		floatingActionButtonPosition = FabPosition.End,
		floatingActionButton = { AddContactFab {
		
		} },
	) { paddingValues ->
		Column(
			modifier = Modifier
				.padding(paddingValues)
				.fillMaxSize()
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
			) {
				items(contacts) {
					ContactItem(contact = it)
				}
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactItem(
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
				ConntactedTodayButton()
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
		text = contact.lastContacted.extractDateForUi(),
		style = MaterialTheme.typography.bodyMedium,
	)
}

@Composable
fun MoreContactInfo(
	contact: Contact
) {
	Text(
		text = stringResource(id = R.string.country, contact.country),
		style = MaterialTheme.typography.bodyMedium,
	)
}

@Composable
fun ConntactedTodayButton(
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
fun PrmTopBar() {
	Row(
		modifier = Modifier
			.background(color = MaterialTheme.colorScheme.primary)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = stringResource(R.string.app_name),
			style = MaterialTheme.typography.headlineSmall,
			modifier = Modifier.padding(horizontal = 6.dp, vertical = 12.dp),
			color = MaterialTheme.colorScheme.onPrimary
		)
	}
}

@Composable
fun AddContactFab(
	onClick: () -> Unit = {}
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
	PrmApp()
}