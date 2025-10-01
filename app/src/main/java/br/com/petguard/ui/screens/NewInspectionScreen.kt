package br.com.petguard.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import br.com.petguard.data.repository.ReportRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.petguard.data.database.Report
import kotlinx.coroutines.launch

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
        Text(
            text = "GUARD Pet",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8C9E70)
        )
        Text(
            text = "Ajude a proteger os animais",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = "Nova Fiscalização",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text("Endereço:", fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        // Mapa simulado (vai ser implementado depois)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Mapa de localização (simulado)")
                Text("Av. Francisco Vaz Filho, 251", fontSize = 14.sp)
            }
        }

        Text("Evidências:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { /* Capturar foto */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A4A2E))
            ) {
                Text("Foto")
            }
            Button(
                onClick = { /* Gravar vídeo */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A4A2E))
            ) {
                Text("Vídeo")
            }
        }

        Text("Descrição:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            maxLines = 5
        )

        Button(
            onClick = {
                val report = Report(
                    address = address,
                    description = description,
                    status = "PENDING"
                )
                scope.launch {
                    reportRepository.saveReport(report)
                    navController.popBackStack()
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