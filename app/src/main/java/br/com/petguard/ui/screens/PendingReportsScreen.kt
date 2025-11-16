package br.com.petguard.ui.screens

import android.content.Context
import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.IconButton
import br.com.petguard.data.database.AppDatabase
import br.com.petguard.data.repository.AuthRepository
import br.com.petguard.data.repository.UserRepository
import br.com.petguard.data.database.User

val playpenSansVariableFontWght = FontFamily(Font(R.font.playpensans_variablefont_wght))

@Composable
fun PendingReportsScreen(navController: NavController, repository: ReportRepository) {
    val scope = rememberCoroutineScope()
    var reports by remember { mutableStateOf<List<Report>>(emptyList()) }
    val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

    val context = LocalContext.current
    val appDatabase = AppDatabase.getDatabase(context)
    val userRepository = remember { UserRepository(appDatabase) }
    var currentUser by remember { mutableStateOf<User?>(null) }
    var isCommonUser by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        currentUser = userRepository.getCurrentUser()
        isCommonUser = currentUser?.userType == "COMMON"

        if(isCommonUser) {
            val userId = currentUser?.id?.toString() ?: ""
            scope.launch {
                repository.pendingReports.collectLatest { allPending ->
                    val userReports = allPending.filter { it.reportedByUserId == userId }
                    reports = userReports
                }
            }
        }else {
            scope.launch {
                repository.pendingReports.collectLatest {
                    reports = it
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
            Spacer(Modifier.height(8.dp))
            Text(
                text = if (isCommonUser) "Minhas Denúncias Pendentes" else "Denúncias Pendentes",
                color = Color(0xFF6A4A2E),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = playpenSansVariableFontWght
            )
            Spacer(Modifier.height(16.dp))
        }

        if(reports.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isCommonUser) "Você não possui denúncias pendentes"
                    else "Não há denúncias pendentes",
                    color = Color(0xFF7E8C54),
                    fontSize = 16.sp,
                    fontFamily = playpenSansVariableFontWght
                )
            }
        }else {
            LazyColumn {
                itemsIndexed(reports) { index, report ->
                    PendingReportCard(
                        index = index + 1,
                        report = report,
                        repository = repository,
                        scope = scope,
                        navController = navController,
                        context = context,
                        isCommonUser = isCommonUser,
                        currentUserId = currentUser?.id?.toString() ?: ""
                    )
                }
            }
        }
    }
}

@Composable
fun PendingReportCard(
    index: Int,
    report: Report,
    repository: ReportRepository,
    scope: CoroutineScope,
    navController: NavController,
    context: Context,
    isCommonUser: Boolean,
    currentUserId: String
) {
    var expanded by remember { mutableStateOf(false) }
    val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    val authRepo = remember { AuthRepository() }
    var currentUserName by remember { mutableStateOf("") }

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
                    if(!report.reportedByUserName.isNullOrEmpty()) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Denúncia feita por: ${report.reportedByUserName}",
                            color = Color(0xFF7E8C54),
                            fontSize = 12.sp,
                            fontFamily = playpenSansVariableFontWght,
                            fontWeight = FontWeight.Normal
                        )
                    }else if (!report.createdBy.isNullOrEmpty()) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Criado por: ${report.createdBy}",
                            color = Color(0xFF7E8C54),
                            fontSize = 12.sp,
                            fontFamily = playpenSansVariableFontWght,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    
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
                        Column {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = """
                                    Mais informações da denúncia:
                                    Descrição: ${report.description ?: "Sem descrição"}
                                    Data: ${report.createdAt?.format(dtf) ?: "Sem data"}
                                """.trimIndent(),
                                color = Color(0xFF7E8C54),
                                fontFamily = playpenSansVariableFontWght
                            )

                            Spacer(Modifier.height(8.dp))


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
                                if(isCommonUser) {
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
                                }else {
                                    Button(
                                        onClick = {
                                            scope.launch {
                                                repository.markAsCompleted(report.id, currentUserName)
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E8C54)),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Concluir", color = Color.White, fontWeight = FontWeight.Bold)
                                    }

                                    Button(
                                        onClick = {
                                            navController.navigate("new_inspection/${report.id}")
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF452001)),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Editar", color = Color.White)
                                    }

                                    Button(
                                        onClick = {
                                            scope.launch {
                                                repository.deleteById(report.id)
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9534F)),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Excluir", color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MediaPreviewDialog(
    uri: Uri,
    isVideo: Boolean,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        text = {
            if (isVideo) {
                AndroidView(
                    factory = { ctx ->
                        VideoView(ctx).apply {
                            setVideoURI(uri)
                            setOnPreparedListener { mp ->
                                mp.isLooping = true
                                start()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

