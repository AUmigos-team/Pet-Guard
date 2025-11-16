package br.com.petguard.ui.screens

import androidx.annotation.IdRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.petguard.R
import br.com.petguard.ui.components.GuardPetLogo
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

@Composable
fun UserTypeSelectionScreen(navController: NavController) {
    val playpenSans = FontFamily(Font(R.font.playpensans_variablefont_wght))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GuardPetLogo(
            modifier = Modifier.fillMaxWidth(),
            fontSizeMain = 50,
            fontSizeSub = 36,
            subtitleSize = 16
        )

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Seja bem vindo, você é um?",
            fontSize = 20.sp,
            fontFamily = playpenSans,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF452001),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            UserTypeCard(
                title = "Usuário\nComum",
                subtitle = "Fazer denúncias",
                image = R.drawable.common_user,
                backgroundColor = Color(0xFFE8F5E8),
                onClick = {
                    navController.navigate("common_login")
                }
            )

            UserTypeCard(
                title = "Fiscal",
                subtitle = "Atender denúncias",
                image = R.drawable.inspector_user,
                backgroundColor = Color(0xFFF5F0E8),
                onClick = {
                    navController.navigate("inspector_login")
                }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Escolha uma das opções acima",
            fontSize = 14.sp,
            fontFamily = playpenSans,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF7E8C54),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@Composable
fun UserTypeCard(
    title: String,
    subtitle: String,
    image: Int,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    val playpenSans = FontFamily(Font(R.font.playpensans_variablefont_wght))

    Card(
        modifier = Modifier
            .width(150.dp)
            .height(200.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = title,
                modifier = Modifier
                    .size(64.dp)
                    .border(
                        width = 2.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontFamily = playpenSans,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF452001),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                fontSize = 12.sp,
                fontFamily = playpenSans,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF7E8C54),
                textAlign = TextAlign.Center
            )
        }
    }
}