package cz.wz.jelinekp.prm.features.signin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cz.wz.jelinekp.prm.core.ui.MainActivity
import cz.wz.jelinekp.prm.features.signin.ui.theme.PersonalRelationshipManagementTheme

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PersonalRelationshipManagementTheme {
                // A surface container using the 'background' color from the theme
                val context = LocalContext.current
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Spacer(modifier = Modifier.weight(2f))
                        Box(modifier = Modifier.weight(3f).align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center) {
                            Button(onClick = { context.startActivity(Intent(context, MainActivity::class.java)) }) {
                                Text(text = "Go to contacts")
                            }
                        }
                    }
                }
            }
        }
    }
}