package br.com.petguard.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import br.com.petguard.ui.components.BottomMenu

@Composable
fun HomeScreen(navController: NavController) {
    val ralewayDots = FontFamily(Font(R.font.raleway_dots_regular))
    val windSong = FontFamily(Font(R.font.windsong_regular))
    val playpenSansVariableFontWght = FontFamily(Font(R.font.playpensans_variablefont_wght))

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
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

            Text(
                text = "Olá, Fiscal!",
                fontSize = 24.sp,
                fontFamily = playpenSansVariableFontWght,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF452001),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(100.dp))

            Column (
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
                    onClick = { navController.navigate("new_inspection") }
                ) {
                    Box(
                        modifier = Modifier
                            .height(80.dp)
                            .padding(16.dp)
                            .fillMaxWidth(),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.add_icon),
                            contentDescription = "add icon",
                            modifier = Modifier
                                .size(35.dp)
                                .align(Alignment.CenterStart)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Nova Fiscalização",
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = playpenSansVariableFontWght,
                            color = Color(0xFF7E8C54),
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        )

                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDE8A4)),
                        onClick = { navController.navigate("pending_reports") }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.warning_icon),
                                    contentDescription = "warning icon",
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "12 denúncias\npendentes",
                                    fontWeight = FontWeight.Light,
                                    fontFamily = playpenSansVariableFontWght,
                                    color = Color(0xFFAF9733),
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFD2D8C0)),
                        onClick = { navController.navigate("reports") }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.check_icon),
                                    contentDescription = "check icon",
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "30 denúncias\nconcluídas",
                                    fontWeight = FontWeight.Light,
                                    fontFamily = playpenSansVariableFontWght,
                                    textAlign = TextAlign.Center,
                                    color = Color(0xFF7E8C54),
                                )
                            }
                        }
                    }
                }
            }
        }
//        BottomMenu(
//            navController = navController,
//            currentScreen = "home",
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.BottomCenter)
//        )
    }
}