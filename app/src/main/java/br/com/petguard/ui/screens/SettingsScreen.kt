package br.com.petguard.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.petguard.data.database.AppDatabase
import kotlinx.coroutines.launch
@Composable
fun SettingsScreen(navController: NavController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val appDatabase = AppDatabase.getDatabase(context)
    val userDao = appDatabase.userDao()

    var notificationsEnabled by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text("Ajustes", color = Color(0xFF6A4A2E))
        Spacer(Modifier.height(16.dp))

        Text("Dados do fiscal:")
        Spacer(Modifier.height(8.dp))
        // Isso pode vir do banco também
        Text("Nome: João Pedro")
        Text("Matrícula: 09431")
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Notificações")
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    userDao.cleanAll()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finalizar sessão", color = Color.White)
        }
    }
}
