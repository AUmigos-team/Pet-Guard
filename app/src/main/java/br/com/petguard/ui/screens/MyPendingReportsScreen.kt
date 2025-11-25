package br.com.petguard.ui.screens

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.IconButton
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import br.com.petguard.data.database.AppDatabase
import br.com.petguard.data.repository.UserRepository
import br.com.petguard.data.database.User
import br.com.petguard.data.repository.AuthRepository
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MyPendingReportsScreen(navController: NavController, repository: ReportRepository) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var reports by remember { mutableStateOf<List<Report>>(emptyList()) }
    val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

    val appDatabase = AppDatabase.getDatabase(context)
    val userRepository = remember { UserRepository(appDatabase) }
    var currentUser by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        currentUser = userRepository.getCurrentUser()
        val userId = currentUser?.uid ?: ""

        if (userId.isNotEmpty()) {
            scope.launch {
                repository.getPendingReportsByUserId(userId).collectLatest { userReports ->
                    reports = userReports
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
            Text(
                text = "Minhas Denúncias Pendentes",
                color = Color(0xFF6A4A2E),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = playpenSansVariableFontWght
            )
        }

        if(reports.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Você não possui denúncias pendentes",
                    color = Color(0xFF7E8C54),
                    fontSize = 16.sp,
                    fontFamily = playpenSansVariableFontWght
                )
            }
        } else {
            LazyColumn {
                itemsIndexed(reports) { index, report ->
                    MyPendingReportCard(
                        index = index + 1,
                        report = report,
                        dtf = dtf,
                        navController = navController,
                        currentUserId = currentUser?.id?.toString() ?: "",
                        onEditClick = {
                            navController.navigate("new_inspection/${report.id}")
                        }
                    )
                }
            }
        }
    }
}



@Composable
fun MyPendingReportCard(
    index: Int,
    report: Report,
    dtf: DateTimeFormatter,
    navController: NavController,
    currentUserId: String,
    onEditClick: () -> Unit
) {
    val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    val authRepo = remember { AuthRepository() }
    var currentUserName by remember { mutableStateOf("") }

    val context = LocalContext.current
    var mediaDialogOpen by remember { mutableStateOf(false) }
    var selectedMediaUri by remember { mutableStateOf<Uri?>(null) }
    var selectedIsVideo by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }

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

    LaunchedEffect(Unit) {
        authRepo.getCurrentUserData(
            onSuccess = { data ->
                currentUserName = data["name"] as? String ?: "Usuário"
            },
            onError = {
                currentUserName = "Usuário"
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDE8A4)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
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

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = report.address ?: "Endereço não informado",
                        color = Color(0xFF7E8C54),
                        fontWeight = FontWeight.Medium,
                        fontFamily = playpenSansVariableFontWght
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Status: Pendente",
                        color = Color(0xFFAF9733),
                        fontSize = 12.sp,
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
                        Column {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = """
                                    Descrição: ${report.description ?: "Sem descrição"}
                                    Data: ${report.createdAt?.format(dtf) ?: "Sem data"}
                                """.trimIndent(),
                                color = Color(0xFF7E8C54),
                                fontFamily = playpenSansVariableFontWght
                            )

                            Spacer(Modifier.height(12.dp))

                            if (photoList.isNotEmpty() || videoList.isNotEmpty()) {
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = "Mídias anexadas:",
                                    color = Color(0xFF7E8C54),
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = playpenSansVariableFontWght
                                )

                                Spacer(Modifier.height(8.dp))

                                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    itemsIndexed(photoList) { _, path ->
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

                                    itemsIndexed(videoList) { _, path ->
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

                                if (mediaDialogOpen && selectedMediaUri != null) {
                                    MediaPreviewDialog(
                                        uri = selectedMediaUri!!,
                                        isVideo = selectedIsVideo,
                                        onDismiss = {
                                            mediaDialogOpen = false
                                            selectedMediaUri = null
                                            selectedIsVideo = false
                                        }
                                    )
                                }
                            }

                            Spacer(Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                    if (report.reportedByUserId == currentUserId) {
                                        Button(
                                            onClick = {
                                                navController.navigate("new_inspection/${report.id}")
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF452001)),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Editar", color = Color.White)
                                        }
                                    } else {
                                        Text(
                                            text = "Apenas visualização",
                                            color = Color(0xFF7E8C54),
                                            fontSize = 14.sp,
                                            fontFamily = playpenSansVariableFontWght,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }


                            }
                        }
                    }
                }
            }
        }
    }
}
