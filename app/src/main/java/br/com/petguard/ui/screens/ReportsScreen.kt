package br.com.petguard.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import br.com.petguard.data.database.Report
import br.com.petguard.data.repository.ReportRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items

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

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text("Relatórios", color = Color(0xFF6A4A2E))
        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(reports) { report ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFDCE5D0))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(text = "Relatório #${report.id}")
                        Text(text = "Endereço: ${report.address}")
                        Text(text = "Status: ${report.status}")
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Abrir PDF",
                            color = Color(0xFF6A4A2E)
                        )
                    }
                }
            }
        }
    }
}