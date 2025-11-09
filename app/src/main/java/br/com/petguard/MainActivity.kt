package br.com.petguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import br.com.petguard.ui.navigation.AppNavigation
import br.com.petguard.ui.theme.PetGuardTheme
import br.com.petguard.ui.theme.ThemeManager
import br.com.petguard.ui.theme.ThemePreference
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
        ThemeManager.isDarkTheme.value = ThemePreference.isDarkMode(this)
        setContent {
            val darkTheme = ThemeManager.isDarkTheme.value
            PetGuardTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}