package br.com.petguard.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.widget.Toast

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val userDao = AppDatabase.getDatabase(context).userDao()

    val ralewayDots = FontFamily(Font(R.font.raleway_dots_regular))
    val windSong = FontFamily(Font(R.font.windsong_regular))
    val playpenSansVariableFontWght = FontFamily(Font(R.font.playpensans_variablefont_wght))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "GUARD",
            fontSize = 55.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = ralewayDots,
            color = Color(0xFF7E8C54)
        )

        Text(
            text = "Pet",
            fontSize = 40.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = windSong,
            color = Color(0xFF7E8C54)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Realize seu login",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = playpenSansVariableFontWght,
            color = Color(0xFF452001)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuário") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                if (username.isNotBlank() && password.isNotBlank()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        // Buscar usuário no banco local
                        val user = userDao.getAll()
                            .find { it.name == username && it.password == password }

                        CoroutineScope(Dispatchers.Main).launch {
                            if (user != null) {
                                // Marcar usuário como logado
                                CoroutineScope(Dispatchers.IO).launch {
                                    userDao.atualizar(user.copy(logged = true))
                                }
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .width(150.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF452001))
        ) {
            Text("Login", fontSize = 20.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(
            onClick = { navController.navigate("register") }
        ) {
            Text("Não tem conta? Cadastre-se")
        }
    }
}
