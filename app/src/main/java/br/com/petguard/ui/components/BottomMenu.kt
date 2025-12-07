package br.com.petguard.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceEvenly
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.petguard.R

@Composable
fun BottomMenu(navController: NavController, currentScreen: String = "home", modifier: Modifier = Modifier, userType: String = "COMMON") {
    val playpenSansVariableFontWght = FontFamily(Font(R.font.playpensans_variablefont_wght))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF7E8C54)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if (currentScreen != "denuncias") {
                            navController.navigate("pending_reports")
                        }
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.home_icon),
                    contentDescription = "home icon",
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Denúncias",
                    fontFamily = playpenSansVariableFontWght,
                    color = if (currentScreen == "denuncias") Color(0xFF7E8C54) else Color(0xFF452001),
                    fontWeight = if (currentScreen == "denuncias") FontWeight.Bold else FontWeight.Medium,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            if (currentScreen != "denuncias") {
                                navController.navigate("pending_reports")
                            }
                        }
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if (currentScreen != "visitas") {
                            navController.navigate("new_inspection")
                        }
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.visit_icon),
                    contentDescription = "visit icon",
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Visitas",
                    fontFamily = playpenSansVariableFontWght,
                    color = if (currentScreen == "visitas") Color(0xFF7E8C54) else Color(0xFF452001),
                    fontWeight = if (currentScreen == "visitas") FontWeight.Bold else FontWeight.Medium,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            if (currentScreen != "visitas") {
                                navController.navigate("new_inspection")
                            }
                        }
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if (currentScreen != "relatorios") {
                            if (userType == "COMMON") {
                                navController.navigate("my_completed_reports")
                            } else {
                                navController.navigate("reports")
                            }
                        }
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.report_icon),
                    contentDescription = "reports icon",
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = if (userType == "COMMON") "Concluídas" else "Relatórios",
                    fontFamily = playpenSansVariableFontWght,
                    color = if (currentScreen == "relatorios") Color(0xFF7E8C54) else Color(0xFF452001),
                    fontWeight = if (currentScreen == "relatorios") FontWeight.Bold else FontWeight.Medium,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            if (currentScreen != "relatorios") {
                                if (userType == "COMMON") {
                                    navController.navigate("my_completed_reports")
                                } else {
                                    navController.navigate("reports")
                                }
                            }
                        }
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if (currentScreen != "ajustes") {
                            navController.navigate("settings")
                        }
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.settings_icon),
                    contentDescription = "settings icon",
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Ajustes",
                    fontFamily = playpenSansVariableFontWght,
                    color = if (currentScreen == "ajustes") Color(0xFF7E8C54) else Color(0xFF452001),
                    fontWeight = if (currentScreen == "ajustes") FontWeight.Bold else FontWeight.Medium,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            if (currentScreen != "ajustes") {
                                navController.navigate("settings")
                            }
                        }
                )
            }

        }
    }
}