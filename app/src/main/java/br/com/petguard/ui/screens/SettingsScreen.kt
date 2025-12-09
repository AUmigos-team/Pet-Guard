package br.com.petguard.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.petguard.R
import br.com.petguard.data.database.AppDatabase
import br.com.petguard.data.database.User
import br.com.petguard.data.repository.AuthRepository
import br.com.petguard.data.repository.UserRepository
import br.com.petguard.ui.components.GuardPetLogo
import br.com.petguard.ui.theme.ThemeManager
import br.com.petguard.ui.theme.ThemePreference
import br.com.petguard.util.NotificationPreference
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val appDatabase = AppDatabase.getDatabase(context)
    val userRepository = remember { UserRepository(appDatabase) }
    val userDao = appDatabase.userDao()
    val authRepo = remember { AuthRepository() }
    val scope = rememberCoroutineScope()
    val playpenSans = FontFamily(Font(R.font.playpensans_variablefont_wght))

    var darkModeEnabled by remember { mutableStateOf(ThemeManager.isDarkTheme.value) }

    var loggedUser by remember { mutableStateOf<User?>(null) }
    var isCommonUser by remember { mutableStateOf(false) }

    val notificationPreference = remember { NotificationPreference(context) }
    var notificationsEnabled by remember {
        mutableStateOf(notificationPreference.areNotificationsEnabled())
    }

    LaunchedEffect(Unit) {
        val localUser = userRepository.getCurrentUser()
        if(localUser != null) {
            loggedUser = localUser
            isCommonUser = localUser.userType == "COMMON"
        } else {
            authRepo.getCurrentUserData(
                onSuccess = { data ->
                    val userType = data["userType"] as? String ?: "COMMON"
                    val firebaseUid = FirebaseAuth.getInstance().currentUser!!.uid

                    loggedUser = User(
                        uid = firebaseUid,
                        name = data["name"] as? String ?: "",
                        email = data["email"] as? String ?: "",
                        birthDate = data["birthDate"] as? String ?: "",
                        registration = data["registration"] as? String,
                        cpf = data["cpf"] as? String ?: "",
                        userType = userType,
                        logged = true
                    )
                    isCommonUser = userType == "COMMON"

                    scope.launch {
                        loggedUser?.let { userRepository.saveUser(it) }
                    }
                },
                onError = { msg ->
                    Toast.makeText(context, "Erro ao carregar dados: $msg", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color(0xFF7E8C54))
            }
        }

        GuardPetLogo(
            modifier = Modifier.fillMaxWidth(),
            fontSizeMain = 40,
            fontSizeSub = 28,
            subtitleSize = 14
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Ajustes",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF452001),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3)),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Usuário",
                    tint = Color.Gray,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Dados do usuário:",
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Nome: ${loggedUser?.name ?: "-"}",
                        color = Color.Gray
                    )

                    if(isCommonUser) {
                        Text(
                            "Email: ${loggedUser?.email ?: "-"}",
                            color = Color.Gray,
                            fontFamily = playpenSans
                        )
                        Text(
                            "Tipo: Usuário Comum",
                            color = Color(0xFF7E8C54),
                            fontSize = 12.sp,
                            fontFamily = playpenSans
                        )
                    }else {
                        Text(
                            "Email: ${loggedUser?.email ?: "-"}",
                            color = Color.Gray,
                            fontFamily = playpenSans
                        )

                        Text(
                            "Tipo: Fiscal",
                            color = Color(0xFF452001),
                            fontSize = 12.sp,
                            fontFamily = playpenSans
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Notificações",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Color(0xFF7E8C54)
            )
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = { newValue ->
                    notificationsEnabled = newValue
                    notificationPreference.setNotificationsEnabled(newValue)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF7E8C54),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.LightGray
                )
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Modo escuro",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = if (darkModeEnabled) Color(0xFF7E8C54) else Color.Gray
            )
            Switch(
                checked = darkModeEnabled,
                onCheckedChange = {
                    darkModeEnabled = it
                    ThemeManager.toggleTheme(it)
                    ThemePreference.setDarkMode(context, it)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF7E8C54),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.LightGray
                )
            )
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                scope.launch {
                    loggedUser?.let { user ->
                        userRepository.updateUser(user.copy(logged = false))
                    }
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("user_type_selection") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A80)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sair", tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text(
                "Finalizar sessão",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
        }
    }
}
