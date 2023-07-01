package cz.wz.jelinekp.prm.features.signin.ui.signin

import android.app.Activity
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.wz.jelinekp.prm.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    state: SignInScreenState,
    onSignInClick: () -> Unit,
) {
    val activityContext = LocalContext.current as Activity
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.sign_in))
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
            Button(
                onClick = { onSignInClick() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(stringResource(id = R.string.log_in_with_google))
            }
            Spacer(modifier = Modifier.weight(3f))
        }
    }
}