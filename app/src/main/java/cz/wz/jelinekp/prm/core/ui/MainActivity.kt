package cz.wz.jelinekp.prm.core.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import cz.wz.jelinekp.prm.navigation.PrmNavHost
import cz.wz.jelinekp.prm.core.ui.theme.PersonalRelationshipManagementTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			PersonalRelationshipManagementTheme {

				val systemUiController = rememberSystemUiController()
				val appBackgroundColor = MaterialTheme.colorScheme.background
				val appNavBarColor = Color.Transparent
				val useDarkIcons = !isSystemInDarkTheme()

				SideEffect {
					systemUiController.setStatusBarColor(
						color = appBackgroundColor,
						darkIcons = useDarkIcons
					)
					systemUiController.setNavigationBarColor(
						color = appNavBarColor,
						darkIcons = useDarkIcons
					)
				}

				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					PrmNavHost()
				}
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
	PersonalRelationshipManagementTheme {
		PrmNavHost()
	}
}