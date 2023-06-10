package cz.wz.jelinekp.prm.features.signin.ui

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
            Text(stringResource(id = R.string.id), style = MaterialTheme.typography.titleMedium)

            Text(state.id ?: "-")

            Spacer(Modifier.height(12.dp))

            Text(stringResource(id = R.string.name), style = MaterialTheme.typography.titleMedium)

            Text(state.name ?: "-")

            Spacer(Modifier.height(12.dp))

            Spacer(Modifier.weight(1f))

            val activity = LocalContext.current as Activity

            Button(
                onClick = { viewModel.signIn(activity) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(id = R.string.log_in))
            }

            Button(
                onClick = { viewModel.signOut() },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(id = R.string.log_out))
            }
        }
    }
}