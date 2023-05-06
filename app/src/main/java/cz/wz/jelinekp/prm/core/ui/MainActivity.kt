package cz.wz.jelinekp.prm.core.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cz.wz.jelinekp.prm.navigation.PrmNavHost
import cz.wz.jelinekp.prm.core.ui.theme.PersonalRelationshipManagementTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			PersonalRelationshipManagementTheme {
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

/*class MainViewModelFactory(private val application: Application) :
	ViewModelProvider.AndroidViewModelFactory() {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return PrmAppViewModel(application) as T
	}
}*/

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
	PersonalRelationshipManagementTheme {
		PrmNavHost()
	}
}