package br.com.petguard.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.navigation.NavController
import br.com.petguard.data.database.Report
import br.com.petguard.data.repository.ReportRepository
import br.com.petguard.ui.components.GuardPetLogo
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReportsScreen(navController: NavController, repository: ReportRepository) {
    val scope = rememberCoroutineScope()
    var reports by remember { mutableStateOf<List<Report>>(emptyList()) }

    LaunchedEffect(Unit) {
        scope.launch {
            repository.completedReports.collectLatest {
                reports = it
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        GuardPetLogo(
            modifier = Modifier.fillMaxWidth(),
            fontSizeMain = 40,
            fontSizeSub = 28,
            subtitleSize = 14
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Relatórios concluídos",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF452001),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(reports) { report ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                        },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD2D8C0))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = "Relatório #${report.id}",
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF452001)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(text = "Endereço: ${report.address}", fontSize = 14.sp)
                        Text(text = "Status: ${report.status}", fontSize = 14.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Abrir PDF",
                            color = Color(0xFF6A4A2E),
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}