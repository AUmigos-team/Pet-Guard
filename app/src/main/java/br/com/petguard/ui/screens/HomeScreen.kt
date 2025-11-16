package br.com.petguard.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import br.com.petguard.data.repository.ReportRepository
import br.com.petguard.ui.components.GuardPetLogo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import br.com.petguard.data.database.AppDatabase
import br.com.petguard.data.database.User
import br.com.petguard.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(navController: NavController, reportRepository: ReportRepository) {
    val context = LocalContext.current
    val playpenSans = FontFamily(Font(R.font.playpensans_variablefont_wght))
    val pendingReports by reportRepository.pendingReports.collectAsState(initial = emptyList())
    val completedReports by reportRepository.completedReports.collectAsState(initial = emptyList())
    val userRepository = remember { UserRepository(AppDatabase.getDatabase(context)) }
    var currentUser by remember { mutableStateOf<User?>(null) }
    var isCommonUser by remember { mutableStateOf(false) }
    var userPendingCount by remember { mutableStateOf(0) }
    var userCompletedCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        currentUser = userRepository.getCurrentUser()
        isCommonUser = currentUser?.userType == "COMMON"

        if (isCommonUser) {
            val userId = currentUser?.id?.toString() ?: ""
            userPendingCount = withContext(Dispatchers.IO) {
                pendingReports.count { it.reportedByUserId == userId }
            }
            userCompletedCount = withContext(Dispatchers.IO) {
                completedReports.count { it.reportedByUserId == userId }
            }
        }
    }

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
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = if (isCommonUser) "Olá, ${currentUser?.name ?: "Usuário"}!" else "Olá, Fiscal!",
                fontSize = 24.sp,
                fontFamily = playpenSans,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF452001),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(100.dp))

            Column(
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
                            fontFamily = playpenSans,
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
                                    text = if (isCommonUser) "$userPendingCount denúncias\npendentes"
                                    else "${pendingReports.size} denúncias\npendentes",
                                    fontWeight = FontWeight.Light,
                                    fontFamily = playpenSans,
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
                                    text = if (isCommonUser) "$userCompletedCount denúncias\nconcluídas"
                                    else "${completedReports.size} denúncias\nconcluídas",
                                    fontWeight = FontWeight.Light,
                                    fontFamily = playpenSans,
                                    textAlign = TextAlign.Center,
                                    color = Color(0xFF7E8C54),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}