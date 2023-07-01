package cz.wz.jelinekp.prm.features.signin.ui.profile

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import cz.wz.jelinekp.prm.R
import cz.wz.jelinekp.prm.features.signin.data.UserData
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
	userData: UserData?,
	onSignOut: () -> Unit,
	viewModel: ProfileViewModel = koinViewModel()
) {
	val state by viewModel.screenState.collectAsStateWithLifecycle()
	val activityContext = LocalContext.current as Activity
	
	BackHandler {
		activityContext.finish()
	}
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(stringResource(id = R.string.profile))
				},
				navigationIcon = {
					IconButton(onClick = { activityContext.finish() }) {
						Icon(Icons.Default.ArrowBack, contentDescription = null)
					}
				},
			)
		},
	) { paddingValues ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(ScrollState(0))
				.padding(paddingValues),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			if (userData?.profilePictureUrl != null) {
				AsyncImage(
					model = userData.profilePictureUrl,
					contentDescription = stringResource(R.string.profile_picture),
					modifier = Modifier
						.size(100.dp)
						.clip(CircleShape),
					contentScale = ContentScale.Crop
				)
				Spacer(modifier = Modifier.height(16.dp))
			}
			if (userData?.username != null) {
				Text(text = stringResource(R.string.signed_in_as))
				Text(
					text = userData.username,
					textAlign = TextAlign.Center,
					style = MaterialTheme.typography.titleLarge
				)
			}
			Button(
				onClick = onSignOut,
				modifier = Modifier.padding(vertical = 20.dp),
			) {
				Text(stringResource(id = R.string.log_out))
			}
			
			ButtonWithDialog(
				onClick = { viewModel.syncToFirebase() },
				buttonText = stringResource(id = R.string.backup_data_online),
				dialogQuestion = stringResource(R.string.do_you_really_want_to_rewrite_online_data_with_stored_data)
			)
			
			ButtonWithDialog(
				onClick = { viewModel.syncFromFirebase() },
				buttonText = stringResource(id = R.string.download_online_data),
				dialogQuestion = stringResource(R.string.do_you_really_want_to_rewrite_stored_data_with_online_data)
			)
			
			if (state.isSyncFromFirebaseSuccess) {
				SuccessMessage(text = stringResource(R.string.data_successfully_synced_from_firebase))
				activityContext.finish()
			}
			if (state.isSyncToFirebaseSuccess) {
				SuccessMessage(text = stringResource(R.string.data_successfully_synced_to_firebase))
				activityContext.finish()
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ButtonWithDialog(
	onClick: () -> Unit,
	buttonText: String,
	dialogQuestion: String,
) {
	val isDialogOpen = rememberSaveable {
		mutableStateOf(false)
	}
	
	if (isDialogOpen.value) {
		AlertDialog(
			onDismissRequest = { isDialogOpen.value = false },
			title = { Text(
				text = dialogQuestion,
				style = MaterialTheme.typography.bodyMedium,
			) },
			dismissButton = { TextButton(
				onClick = { isDialogOpen.value = false },
			) {
				Text(stringResource(id = R.string.cancel))
			} },
			confirmButton = { TextButton(
				onClick = onClick,
			) {
				Text(stringResource(R.string.confirm))
			} }
		)
	}
	
	Button(
		onClick = { isDialogOpen.value = true },
		modifier = Modifier.padding(vertical = 20.dp)
	) {
		Text(buttonText)
	}
}

@Composable
fun SuccessMessage(
	text: String
) {
	val context = LocalContext.current
	Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}