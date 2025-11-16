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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.petguard.R
import br.com.petguard.data.database.AppDatabase
import br.com.petguard.data.database.User
import br.com.petguard.data.repository.AuthRepository
import br.com.petguard.data.repository.UserRepository
import br.com.petguard.ui.components.GuardPetLogo
import kotlinx.coroutines.launch

@Composable
fun CommonUserLoginScreen(navController: NavController) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val repository = AuthRepository()
    val playpenSans = FontFamily(Font(R.font.playpensans_variablefont_wght))

    val scope = rememberCoroutineScope()
    val authRepo = AuthRepository()
    val db = AppDatabase.getDatabase(context)
    val userRepo = UserRepository(db)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
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

        Spacer(Modifier.height(60.dp))

        GuardPetLogo(modifier = Modifier.fillMaxWidth())

        Text(
            "Login - Usuário Comum",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = playpenSans,
            color = Color(0xFF452001),
            modifier = Modifier.padding(vertical = 10.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Digite seu email", color = Color(0xFF7E8C54)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Digite sua senha", color = Color(0xFF7E8C54)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
        )

        Spacer(Modifier.height(40.dp))

        Button(
            onClick = {
                authRepo.loginUser(email, password,
                    onSuccess = {
                        authRepo.getCurrentUserData(
                            onSuccess = { data ->
                                val user = User(
                                    name = data["name"].toString(),
                                    email = email,
                                    birthDate = data["birthDate"].toString(),
                                    cpf = data["cpf"].toString(),
                                    userType = "COMMON",
                                    logged = true
                                )

                                scope.launch {
                                    userRepo.clearSession()
                                    val userId = userRepo.saveUser(user)
                                }
                                navController.navigate("home")
                            },
                            onError = { msg ->
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        )

                    },
                    onError = { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF452001))
        ) {
            Text("Login", fontSize = 20.sp, color = Color.White)
        }

        Spacer(Modifier.height(20.dp))

        TextButton(onClick = { navController.navigate("common_register") }) {
            Text("Não tem uma conta? Registre-se")
        }
    }
}