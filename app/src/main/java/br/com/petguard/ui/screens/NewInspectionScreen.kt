package br.com.petguard.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import br.com.petguard.data.repository.ReportRepository
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.petguard.data.database.Report
import br.com.petguard.ui.components.GuardPetLogo
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun NewInspectionScreen(navController: NavController, reportRepository: ReportRepository) {
    var address by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        GuardPetLogo(
            modifier = Modifier.fillMaxWidth(),
            fontSizeMain = 40,
            fontSizeSub = 28,
            subtitleSize = 14
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Nova Fiscalização",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF452001),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text("Endereço:", fontWeight = FontWeight.Bold, color = Color(0xFF452001))
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEFEF))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Mapa de localização (simulado)", color = Color.DarkGray)
                Text("Av. Francisco Vaz Filho, 251", fontSize = 14.sp, color = Color.Gray)
            }
        }

        Text("Evidências:", fontWeight = FontWeight.Bold, color = Color(0xFF452001), modifier = Modifier.padding(top = 16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { /* Capturar foto */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A4A2E))
            ) {
                Text("Foto", color = Color.White)
            }
            Button(
                onClick = { /* Gravar vídeo */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A4A2E))
            ) {
                Text("Vídeo", color = Color.White)
            }
        }

        Text("Descrição:", fontWeight = FontWeight.Bold, color = Color(0xFF452001), modifier = Modifier.padding(top = 16.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            maxLines = 5
        )

        Button(
            onClick = {
                val report = Report(
                    address = address,
                    description = description,
                    status = "PENDING",
                    createdAt = LocalDateTime.now()
                )
                scope.launch {
                    reportRepository.saveReport(report)
                    navController.popBackStack() // volta para Home
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A4A2E))
        ) {
            Text("Enviar fiscalização", color = Color.White)
        }
    }
}