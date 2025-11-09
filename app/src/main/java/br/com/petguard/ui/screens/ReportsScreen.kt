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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.petguard.data.database.Report
import br.com.petguard.data.repository.ReportRepository
import br.com.petguard.ui.components.GuardPetLogo
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import android.content.Context
import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson

@Composable
fun ReportsScreen(navController: NavController, repository: ReportRepository) {
    val scope = rememberCoroutineScope()
    var reports by remember { mutableStateOf<List<Report>>(emptyList()) }
    val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

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
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color(0xFF7E8C54))
            }
        }

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
                text = "Relatórios concluídos",
                color = Color(0xFF6A4A2E),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = playpenSansVariableFontWght
            )
            Spacer(Modifier.height(16.dp))
        }

        LazyColumn {
            itemsIndexed(reports) { index, report ->
                CompletedReportCard(
                    index = index + 1,
                    report = report,
                    dtf = dtf
                )
            }
        }
    }
}

@Composable
fun CompletedReportCard(
    index: Int,
    report: Report,
    dtf: DateTimeFormatter
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var mediaDialogOpen by remember { mutableStateOf(false) }
    var selectedMediaUri by remember { mutableStateOf<Uri?>(null) }
    var selectedIsVideo by remember { mutableStateOf(false) }

    val gson = Gson()
    val photoList: List<String> = report.photoPath?.let {
        try {
            gson.fromJson(it, Array<String>::class.java).toList()
        } catch (e: Exception) {
            emptyList()
        }
    } ?: emptyList()

    val videoList: List<String> = report.videoPath?.let {
        try {
            gson.fromJson(it, Array<String>::class.java).toList()
        } catch (e: Exception) {
            emptyList()
        }
    } ?: emptyList()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD2D8C0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
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
                    text = "Relatório #${report.id}",
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF7E8C54),
                    fontFamily = playpenSansVariableFontWght
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = report.address ?: "Endereço não informado",
                    fontSize = 14.sp,
                    color = Color(0xFF7E8C54),
                    fontFamily = playpenSansVariableFontWght
                )

                Spacer(Modifier.height(8.dp))
                Text(
                    text = if (expanded) "Recolher" else "Ver detalhes...",
                    color = Color(0xFF7E8C54),
                    fontSize = 14.sp,
                    fontFamily = playpenSansVariableFontWght,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { expanded = !expanded }
                )

                AnimatedVisibility(expanded) {
                    Column {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = """
                                Descrição: ${report.description ?: "Sem descrição"}
                                Status: ${report.status}
                                Criado em: ${report.createdAt?.format(dtf) ?: "Sem data"}
                                Atualizado em: ${report.updatedAt?.format(dtf) ?: "Sem data"}
                            """.trimIndent(),
                            color = Color(0xFF7E8C54),
                            fontFamily = playpenSansVariableFontWght,
                            fontSize = 14.sp
                        )

                        if (photoList.isNotEmpty() || videoList.isNotEmpty()) {
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "Mídias anexadas:",
                                color = Color(0xFF7E8C54),
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = playpenSansVariableFontWght,
                                fontSize = 14.sp
                            )
                            Spacer(Modifier.height(8.dp))

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(photoList) { path ->
                                    val imageUri = Uri.parse(path)
                                    Image(
                                        painter = rememberAsyncImagePainter(imageUri),
                                        contentDescription = "Foto anexada",
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable {
                                                selectedMediaUri = imageUri
                                                selectedIsVideo = false
                                                mediaDialogOpen = true
                                            },
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                items(videoList) { path ->
                                    val videoUri = Uri.parse(path)
                                    Box(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color.Black)
                                            .clickable {
                                                selectedMediaUri = videoUri
                                                selectedIsVideo = true
                                                mediaDialogOpen = true
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.PlayArrow,
                                            contentDescription = "Ver vídeo",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }

                        if (mediaDialogOpen && selectedMediaUri != null) {
                            MediaPreviewDialog(
                                uri = selectedMediaUri!!,
                                isVideo = selectedIsVideo,
                                onDismiss = {
                                    mediaDialogOpen = false
                                    selectedMediaUri = null
                                }
                            )
                        }

//                        if (report.latitude != null && report.longitude != null) {
//                            Spacer(Modifier.height(4.dp))
//                            Text(
//                                text = "Localização: ${report.latitude}, ${report.longitude}",
//                                color = Color(0xFF7E8C54),
//                                fontFamily = playpenSansVariableFontWght,
//                                fontSize = 14.sp
//                            )
//                        }
//
//                        if (report.photoPath != null) {
//                            Spacer(Modifier.height(4.dp))
//                            Text(
//                                text = "Foto anexada: ${report.photoPath}",
//                                color = Color(0xFF7E8C54),
//                                fontFamily = playpenSansVariableFontWght,
//                                fontSize = 14.sp
//                            )
//                        }
//
//                        if (report.videoPath != null) {
//                            Spacer(Modifier.height(4.dp))
//                            Text(
//                                text = "Vídeo anexado: ${report.videoPath}",
//                                color = Color(0xFF7E8C54),
//                                fontFamily = playpenSansVariableFontWght,
//                                fontSize = 14.sp
//                            )
//                        }
                    }
                }
            }
        }
    }
}

