package cz.wz.jelinekp.prm.features.signin.ui

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.wz.jelinekp.prm.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    //navController: NavController,
    viewModel: SignInViewModel = koinViewModel(),
) {
    val state by viewModel.screenStateStream.collectAsStateWithLifecycle()
    val activityContext = LocalContext.current as Activity

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
                .padding(paddingValues)
                .padding(12.dp)
                .fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.weight(3f))
            Text(
                text = if (state.id != null) "Logged in as ${state.name}" else "Not logged in",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.weight(1f))
            if (state.isSignedIn) {
                Button(
                    onClick = { viewModel.signOut() },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                ) {
                    Text(stringResource(id = R.string.log_out))
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { viewModel.syncToFirebase() },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                ) {
                    Text(stringResource(id = R.string.backup_data_online))
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { viewModel.syncFromFirebase() },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                ) {
                    Text(stringResource(id = R.string.download_online_data))
                }
            } else {
                Button(
                    onClick = {
                        viewModel.signIn(activityContext)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                ) {
                    Text(stringResource(id = R.string.log_in_with_google))
                }
            }
            Spacer(modifier = Modifier.weight(3f))
        }
    }

    if (state.isSyncFromFirebaseSuccess)
        SuccessMessage(text = "Data successfully synced from Firebase")
    if (state.isSyncToFirebaseSuccess)
        SuccessMessage(text = "Data successfully synced to Firebase")
}

@Composable
fun SuccessMessage(
    text: String
) {
    val context = LocalContext.current
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}