package cz.wz.jelinekp.prm.features.signin

import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.identity.Identity
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.wz.jelinekp.prm.features.signin.data.GoogleAuthUiClient
import cz.wz.jelinekp.prm.features.signin.ui.profile.ProfileScreen
import cz.wz.jelinekp.prm.features.signin.ui.signin.SignInScreen
import cz.wz.jelinekp.prm.features.signin.ui.signin.SignInViewModel
import cz.wz.jelinekp.prm.features.signin.ui.theme.PersonalRelationshipManagementTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

// Inspiration: https://github.com/philipplackner/ComposeGoogleSignInCleanArchitecture/tree/master
class SignInActivity : ComponentActivity() {
	
	private val googleAuthUiClient by lazy {
		GoogleAuthUiClient(
			context = applicationContext,
			oneTapClient = Identity.getSignInClient(applicationContext),
		)
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			PersonalRelationshipManagementTheme {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					val signInNavController = rememberNavController()
					NavHost(navController = signInNavController, startDestination = "sign_in") {
						composable("sign_in") {
							val viewModel: SignInViewModel = koinViewModel()
							val state by viewModel.screenStateStream.collectAsStateWithLifecycle()
							
							LaunchedEffect(key1 = Unit) {
								if (googleAuthUiClient.getSignedInUser() != null) {
									signInNavController.navigate("profile")
								}
							}
							
							val launcher = rememberLauncherForActivityResult(
								contract = ActivityResultContracts.StartIntentSenderForResult(),
								onResult = { result ->
									if (result.resultCode == RESULT_OK) {
										lifecycleScope.launch {
											val signInResult = googleAuthUiClient.signInWithIntent(
												intent = result.data ?: return@launch
											)
											viewModel.onSignInResult(signInResult)
										}
									}
								}
							)
							
							LaunchedEffect(key1 = state.isSignedIn) {
								if (state.isSignedIn) {
									Toast.makeText(
										applicationContext,
										"Sign in successful",
										Toast.LENGTH_LONG
									).show()
									
									signInNavController.navigate("profile")
									viewModel.resetState()
								}
							}
							
							SignInScreen(
								state = state,
								onSignInClick = {
									lifecycleScope.launch {
										val signInIntentSender = googleAuthUiClient.signIn()
										launcher.launch(
											IntentSenderRequest.Builder(
												signInIntentSender ?: return@launch
											).build()
										)
									}
								}
							)
						}
						composable("profile") {
							ProfileScreen(
								userData = googleAuthUiClient.getSignedInUser(),
								onSignOut = {
									lifecycleScope.launch {
										googleAuthUiClient.signOut()
										
										Toast.makeText(
											applicationContext,
											"Signed out",
											Toast.LENGTH_LONG
										).show()
										
										signInNavController.popBackStack()
									}
								}
							)
						}
					}
					
				}
			}
		}
	}
}