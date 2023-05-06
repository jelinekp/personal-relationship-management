package cz.wz.jelinekp.prm.features.contacts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun PrmTopBar(
	topBarText: String
) {
	Row(
		modifier = Modifier
			.background(color = MaterialTheme.colorScheme.primary)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = topBarText,
			style = MaterialTheme.typography.headlineSmall,
			modifier = Modifier.padding(horizontal = 6.dp, vertical = 12.dp),
			color = MaterialTheme.colorScheme.onPrimary
		)
	}
}