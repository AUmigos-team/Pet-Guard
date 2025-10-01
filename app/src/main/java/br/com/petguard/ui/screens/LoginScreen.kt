package br.com.petguard.ui.screens

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import br.com.petguard.R

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val ralewayDots = FontFamily(Font(R.font.raleway_dots_regular))
    val windSong = FontFamily(Font(R.font.windsong_regular))
    val playpenSansVariableFontWght = FontFamily(Font(R.font.playpensans_variablefont_wght))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
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
                color = Color(0xFF7E8C54),
                modifier = Modifier
                    .offset(x = (70).dp, y = 25.dp)
            )
        }

        Text(
            text = "Ajude a proteger os animais",
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            fontFamily = playpenSansVariableFontWght,
            color = Color(0xFF452001),
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(150.dp))

        Text(
            text = "Usuário",
            color = Color(0xFF7E8C54),
            fontWeight = FontWeight.Normal,
            fontFamily = playpenSansVariableFontWght,
        )
        OutlinedTextField(
            value = username,
            onValueChange = {username = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(15.dp),
            placeholder = { Text("Digite seu usuário") },
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Senha",
            color = Color(0xFF7E8C54),
            fontWeight = FontWeight.Normal,
            fontFamily = playpenSansVariableFontWght,
        )
        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(15.dp),
            placeholder = {Text("Digite sua senha")},
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                navController.navigate("home")
            },
            modifier = Modifier
                .width(150.dp)
                .padding(top = 60.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF452001))
        ) {
            Text(
                text = "Login",
                fontSize = 30.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = playpenSansVariableFontWght,
                color = Color(0xFFF8F8F8)
            )
        }
    }
}