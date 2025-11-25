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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun InspectorUserLoginScreen(navController: NavController) {
    val context = LocalContext.current
    var registration by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val repository = AuthRepository()
    val playpenSans = FontFamily(Font(R.font.playpensans_variablefont_wght))

    val authRepo = AuthRepository()
    val db = AppDatabase.getDatabase(context)
    val userRepo = UserRepository(db)
    val scope = rememberCoroutineScope()

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
            "Realize seu login",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = playpenSans,
            color = Color(0xFF452001),
            modifier = Modifier.padding(vertical = 10.dp)
        )

        OutlinedTextField(
            value = registration,
            onValueChange = { registration = it },
            label = { Text("Digite sua matrícula", color = Color(0xFF7E8C54)) },
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
                if (registration.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                authRepo.loginInspector(
                    registration = registration,
                    password = password,
                    onSuccess = {
                        authRepo.getCurrentUserData(
                            onSuccess = { data ->
                                val firebaseUid = FirebaseAuth.getInstance().currentUser!!.uid

                                val user = User(
                                    uid = firebaseUid,
                                    name = data["name"].toString(),
                                    email = "$registration@petguard.com.br",
                                    birthDate = data["birthDate"].toString(),
                                    cpf = data["cpf"].toString(),
                                    userType = "INSPECTOR",
                                    logged = true
                                )

                                scope.launch {
                                    userRepo.clearSession()
                                    userRepo.saveUser(user)

                                    Toast.makeText(context, "Login realizado!", Toast.LENGTH_SHORT).show()
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            },
                            onError = { msg ->
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                            }
                        )
                    },
                    onError = { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
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

        TextButton(onClick = { navController.navigate("inspector_register") }) {
            Text("Não tem uma conta? Registre-se")
        }
    }
}
