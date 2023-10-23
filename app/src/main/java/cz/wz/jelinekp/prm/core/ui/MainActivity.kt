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
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import cz.wz.jelinekp.prm.core.ui.theme.PersonalRelationshipManagementTheme
import cz.wz.jelinekp.prm.navigation.PrmNavHost

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Set the FLAG_LAYOUT_NO_LIMITS flag on the window attributes
		/*val params = window.attributes
		params.flags = params.flags or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
		window.attributes = params*/

		// Apply padding to the bottom of your content to account for the navigation bar
		//val navBarHeight = getNavigationBarHeight() // Function to get the height of the navigation bar

		setContent {
			PersonalRelationshipManagementTheme {
				val systemUiController = rememberSystemUiController()
				val appBackgroundColor = MaterialTheme.colorScheme.background
				val appNavBarColor = MaterialTheme.colorScheme.background
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
					color = appBackgroundColor,
				) {
					PrmNavHost(
						//navBarHeight = navBarHeight
				)
				}
			}
		}
	}

	/*private fun getNavigationBarHeight(): Int {
		val resources = resources
		val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
		return if (resourceId > 0) {
			resources.getDimensionPixelSize(resourceId) - 15
		} else {
			0
		}
	}*/
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
	PersonalRelationshipManagementTheme {
		PrmNavHost()
	}
}