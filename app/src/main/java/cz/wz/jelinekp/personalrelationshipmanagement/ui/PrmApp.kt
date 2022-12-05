package cz.wz.jelinekp.personalrelationshipmanagement.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.wz.jelinekp.personalrelationshipmanagement.R
import cz.wz.jelinekp.personalrelationshipmanagement.data.Contact
import cz.wz.jelinekp.personalrelationshipmanagement.data.contacts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrmApp() {
	Scaffold(topBar = {
		PrmTopBar()
	}) { paddingValues ->
		LazyColumn(
			modifier = Modifier
				.background(
					MaterialTheme.colorScheme.background
				)
				.padding(16.dp),
			contentPadding = paddingValues,
			verticalArrangement = Arrangement.spacedBy(12.dp),
			) {
			items(contacts) {
				ContactItem(contact = it)
			}
		}
	}
}

@Composable
fun ContactItem(
	contact: Contact,
	modifier: Modifier = Modifier
) {
	Card(
		modifier = modifier,
		elevation = cardElevation(4.dp),
		) {
		Row(
			modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp)
		) {
			Text(text = contact.name)
		}
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
	PrmApp()
}