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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.petguard.R
import br.com.petguard.data.repository.AuthRepository
import br.com.petguard.ui.components.GuardPetLogo

@Composable
fun CommonUserRegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val birthDateState = remember { mutableStateOf("") }

    val context = LocalContext.current
    val authRepo = AuthRepository()
    val fontFamily = FontFamily(Font(R.font.playpensans_variablefont_wght))

    fun dateMask(input: String): String {
        val digits = input.filter { it.isDigit() }
        return when {
            digits.isEmpty() -> ""
            digits.length <= 2 -> digits
            digits.length <= 4 -> "${digits.substring(0, 2)}/${digits.substring(2)}"
            else -> {
                val day = digits.substring(0, 2)
                val month = digits.substring(2, 4)
                val year = digits.substring(4, 8.coerceAtMost(digits.length))
                "$day/$month/$year"
            }
        }
    }

    fun isDateComplete(date: String): Boolean {
        return date.length == 10 && date.count { it == '/' } == 2
    }

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

        Spacer(modifier = Modifier.height(60.dp))

        GuardPetLogo(
            modifier = Modifier.fillMaxWidth(),
            fontSizeMain = 40,
            fontSizeSub = 28,
            subtitleSize = 14
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Cadastro - Usuário Comum",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = fontFamily,
            color = Color(0xFF452001)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Digite seu nome completo", color = Color(0xFF7E8C54)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = birthDateState.value,
            onValueChange = { text ->
                val digits = text.filter { it.isDigit() }.take(8)
                birthDateState.value = digits
            },
            label = { Text("Data de nascimento (DD/MM/AAAA)", color = Color(0xFF7E8C54)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            visualTransformation = DateVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = cpf,
            onValueChange = { cpf = it },
            label = { Text("Digite seu CPF (apenas números)", color = Color(0xFF7E8C54)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Digite seu email", color = Color(0xFF7E8C54)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Digite sua senha", color = Color(0xFF7E8C54)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                if (name.isBlank() || birthDate.isBlank() || cpf.isBlank() || email.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (!isDateComplete(birthDate)) {
                    Toast.makeText(context, "Data de nascimento incompleta. Use o formato DD/MM/AAAA", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                authRepo.registerCommonUser(
                    name = name,
                    birthDate = birthDate,
                    cpf = cpf,
                    email = email,
                    password = password,
                    onSuccess = {
                        Toast.makeText(context, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                        navController.navigate("common_login") {
                            popUpTo("common_register") { inclusive = true }
                        }
                    },
                    onError = { msg : String ->
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                    }
                )
            },
            modifier = Modifier
                .wrapContentWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF452001))
        ) {
            Text("Cadastrar", fontSize = 20.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(onClick = { navController.navigate("common_login") }) {
            Text("Já tem conta? Faça login")
        }
    }
}

class DateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text

        val transformed = buildString {
            for (i in raw.indices) {
                append(raw[i])
                if (i == 1 || i == 3) append("/")
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 1 -> offset
                    offset <= 3 -> offset + 1
                    offset <= 8 -> offset + 2
                    else -> transformed.length
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 2 -> offset
                    offset <= 5 -> offset - 1
                    offset <= 10 -> offset - 2
                    else -> raw.length
                }
            }
        }

        return TransformedText(AnnotatedString(transformed), offsetMapping)
    }
}