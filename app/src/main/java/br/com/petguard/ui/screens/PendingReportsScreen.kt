package br.com.petguard.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import br.com.petguard.data.database.Report
import br.com.petguard.data.repository.ReportRepository
import br.com.petguard.ui.components.GuardPetLogo
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter


@Composable
fun PendingReportsScreen(navController: NavController, repository: ReportRepository) {
    val scope = rememberCoroutineScope()
    var reports by remember { mutableStateOf<List<Report>>(emptyList()) }
    val ralewayDots = FontFamily(Font(R.font.raleway_dots_regular))
    val windSong = FontFamily(Font(R.font.windsong_regular))
    val playpenSansVariableFontWght = FontFamily(Font(R.font.playpensans_variablefont_wght))
    val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

    LaunchedEffect(Unit) {
        scope.launch {
            repository.pendingReports.collectLatest {
                reports = it
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        GuardPetLogo(
            modifier = Modifier.fillMaxWidth(),
            fontSizeMain = 40,
            fontSizeSub = 28,
            subtitleSize = 14
        )

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Denúncias pendentes",
                color = Color(0xFF6A4A2E),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = playpenSansVariableFontWght
            )
            Spacer(Modifier.height(16.dp))
        }

        LazyColumn {
            itemsIndexed(reports) { index, report ->
                ReportCard(
                    index = index + 1,
                    report = report
                )
            }
        }
    }
}

@Composable
fun ReportCard(
    index: Int,
    report: Report
) {
    var expanded by remember { mutableStateOf(false) }
    val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    val playpenSansVariableFontWght = FontFamily(Font(R.font.playpensans_variablefont_wght))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE082)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFF7E8C54), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = index.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = playpenSansVariableFontWght
                )
            }

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    text = report.address ?: "Endereço não informado",
                    color = Color(0xFF7E8C54),
                    fontWeight = FontWeight.Medium,
                    fontFamily = playpenSansVariableFontWght
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = if (expanded) "Recolher" else "Ver detalhes...",
                    color = Color(0xFF7E8C54),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { expanded = !expanded },
                    fontFamily = playpenSansVariableFontWght,
                    fontWeight = FontWeight.Bold
                )

                AnimatedVisibility(expanded) {
                    Column{
                        Spacer(Modifier.height(8.dp))
                        Text(text = "Mais informações da denúncia:\n" +
                                "Descrição: ${report.description ?: "Sem descrição"}\n" +
                                "Data: ${report.createdAt?.format(dtf) ?: "Sem data"}",
                            color = Color(0xFF7E8C54),
                            fontFamily = playpenSansVariableFontWght
                        )
                    }
                }
            }
        }
    }
}