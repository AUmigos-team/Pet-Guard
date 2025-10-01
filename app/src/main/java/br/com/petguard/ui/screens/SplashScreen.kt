package br.com.petguard.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.petguard.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val ralewayDots = FontFamily(Font(R.font.raleway_dots_regular))
    val windSong = FontFamily(Font(R.font.windsong_regular))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7E8C54)),
        contentAlignment = Alignment.Center
    ) {
            Text(
                text = "GUARD",
                fontSize = 55.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = ralewayDots,
                color = Color.White
            )

            Text(
                text = "Pet",
                fontSize = 40.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = windSong,
                color = Color.White,
                modifier = Modifier
                    .offset(x = (70).dp, y = 25.dp)
            )

    }

    LaunchedEffect(Unit) {
        delay(15000)
        navController.navigate("login")
    }
}