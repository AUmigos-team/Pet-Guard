package br.com.petguard.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Geocoder
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.VideoView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import br.com.petguard.R
import br.com.petguard.data.database.Report
import br.com.petguard.data.repository.ReportRepository
import br.com.petguard.ui.components.GuardPetLogo
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*

sealed class MediaItem {
    data class Photo(val uri: Uri, val bitmap: Bitmap) : MediaItem()
    data class Video(val uri: Uri) : MediaItem()
}

@SuppressLint("MissingPermission")
@Composable
fun NewInspectionScreen(
    navController: androidx.navigation.NavController,
    reportRepository: ReportRepository
) {
    val context = LocalContext.current
    val resolver = context.contentResolver
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    var address by remember { mutableStateOf("Buscando endereço...") }
    var description by remember { mutableStateOf("") }
    var currentLocation by remember { mutableStateOf(LatLng(-20.5386, -47.4004)) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 16f)
    }
    val markerState = remember { MarkerState(position = currentLocation) }

    val scope = rememberCoroutineScope()
    val playpenSans = FontFamily(Font(R.font.playpensans_variablefont_wght))

    val mediaItems = remember { mutableStateListOf<MediaItem>() }
    var pendingPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var pendingVideoUri by remember { mutableStateOf<Uri?>(null) }

    val photoList = mediaItems.filterIsInstance<MediaItem.Photo>().map { it.uri.toString() }
    val videoList = mediaItems.filterIsInstance<MediaItem.Video>().map { it.uri.toString() }

    fun createImageOutputUri(): Uri? {
        val filename = "petguard_photo_${System.currentTimeMillis()}.jpg"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/PetGuard")
            }
        }
        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    fun createVideoOutputUri(): Uri? {
        val filename = "petguard_video_${System.currentTimeMillis()}.mp4"
        val values = ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, filename)
            put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/PetGuard")
            }
        }
        return resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            pendingPhotoUri?.let { uri ->
                val bmp = loadBitmapWithCorrectOrientation(resolver, uri)
                if (bmp != null) {
                    mediaItems.add(MediaItem.Photo(uri, bmp))
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        MediaScannerConnection.scanFile(context, arrayOf(uri.path), null, null)
                    }
                }
            }
        }
        pendingPhotoUri = null
    }

    val videoIntentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = pendingVideoUri ?: result.data?.data
            uri?.let { mediaItems.add(MediaItem.Video(it)) }
        }
        pendingVideoUri = null
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { _ -> }

    fun ensureAndLaunchTakePicture() {
        val hasCam = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED

        val perms = mutableListOf<String>()
        if (!hasCam) perms.add(Manifest.permission.CAMERA)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED
            ) perms.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (perms.isEmpty()) {
            val out = createImageOutputUri()
            pendingPhotoUri = out
            if (out != null) takePictureLauncher.launch(out)
        } else {
            permissionLauncher.launch(perms.toTypedArray())
        }
    }

    fun ensureAndLaunchRecordVideo() {
        val hasCam = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        val hasAudio = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED

        val perms = mutableListOf<String>()
        if (!hasCam) perms.add(Manifest.permission.CAMERA)
        if (!hasAudio) perms.add(Manifest.permission.RECORD_AUDIO)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED
            ) perms.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (perms.isEmpty()) {
            val out = createVideoOutputUri()
            pendingVideoUri = out
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
                out?.let { putExtra(MediaStore.EXTRA_OUTPUT, it) }
            }
            videoIntentLauncher.launch(intent)
        } else {
            permissionLauncher.launch(perms.toTypedArray())
        }
    }

    LaunchedEffect(Unit) {
        val hasLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasLocation) {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                loc?.let {
                    currentLocation = LatLng(it.latitude, it.longitude)
                    markerState.position = currentLocation
                    try {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        val result = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                        if (!result.isNullOrEmpty()) {
                            address = result[0].getAddressLine(0) ?: address
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } else {
            permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        }
    }

    LaunchedEffect(currentLocation) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(currentLocation, 16f),
            durationMs = 1000
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        GuardPetLogo(modifier = Modifier.fillMaxWidth(), fontSizeMain = 40, fontSizeSub = 28, subtitleSize = 14)

        Spacer(Modifier.height(24.dp))

        Text("Nova fiscalização", fontSize = 22.sp, color = Color(0xFF452001))

        Spacer(Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = address,
                onValueChange = { address = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Endereço", fontFamily = playpenSans) },
                textStyle = TextStyle.Default
            )
        }

        Spacer(Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(mapToolbarEnabled = true)
            ) {
                Marker(state = markerState, title = "Local da fiscalização", snippet = address)
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("Evidências:", fontSize = 18.sp, color = Color(0xFF452001))
        Spacer(Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { ensureAndLaunchTakePicture() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E8C54)),
                shape = RoundedCornerShape(50),
                modifier = Modifier.weight(1f).height(48.dp)
            ) {
                Icon(Icons.Default.PhotoCamera, contentDescription = "Foto", tint = Color.White)
                Spacer(Modifier.width(6.dp))
                Text("Foto", color = Color.White)
            }

            Button(
                onClick = { ensureAndLaunchRecordVideo() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                shape = RoundedCornerShape(50),
                modifier = Modifier.weight(1f).height(48.dp)
            ) {
                Icon(Icons.Default.Videocam, contentDescription = "Vídeo", tint = Color.White)
                Spacer(Modifier.width(6.dp))
                Text("Vídeo", color = Color.White)
            }
        }

        if (mediaItems.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(mediaItems) { item ->
                    when (item) {
                        is MediaItem.Photo -> {
                            Image(
                                bitmap = item.bitmap.asImageBitmap(),
                                contentDescription = "Foto",
                                modifier = Modifier
                                    .size(width = 120.dp, height = 180.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }
                        is MediaItem.Video -> {
                            AndroidView(
                                factory = { ctx ->
                                    VideoView(ctx).apply {
                                        setVideoURI(item.uri)
                                        setOnPreparedListener { mp ->
                                            mp.isLooping = true
                                            start()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .size(width = 200.dp, height = 180.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("Descrição:", fontSize = 16.sp, color = Color(0xFF452001))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Foi encontrado...", fontFamily = playpenSans) },
            maxLines = 4,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                val report = Report(
                    address = address,
                    description = description,
                    photoPath = Gson().toJson(photoList),
                    videoPath = Gson().toJson(videoList),
                    status = "PENDING",
                    createdAt = LocalDateTime.now()
                )
                scope.launch {
                    reportRepository.saveReport(report)
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E8C54)),
            shape = RoundedCornerShape(50)
        ) {
            Icon(Icons.Default.Check, contentDescription = "Enviar", tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Enviar fiscalização", color = Color.White)
        }
    }
}

private fun loadBitmapWithCorrectOrientation(resolver: android.content.ContentResolver, uri: Uri): Bitmap? {
    try {
        resolver.openInputStream(uri)?.use { inputForBounds ->
            val optionsBounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeStream(inputForBounds, null, optionsBounds)
            val width = optionsBounds.outWidth
            val height = optionsBounds.outHeight

            val maxSide = 1200
            var inSampleSize = 1
            var halfWidth = width / 2
            var halfHeight = height / 2
            while (halfWidth / inSampleSize >= maxSide && halfHeight / inSampleSize >= maxSide) {
                inSampleSize *= 2
            }

            val options = BitmapFactory.Options().apply { this.inSampleSize = inSampleSize }

            resolver.openInputStream(uri)?.use { inputForDecode ->
                val bmp = BitmapFactory.decodeStream(inputForDecode, null, options) ?: return null

                resolver.openInputStream(uri)?.use { inputForExif ->
                    val exif = ExifInterface(inputForExif)
                    val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                    val matrix = Matrix()
                    when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
                        ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
                    }
                    return if (!matrix.isIdentity) {
                        Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
                    } else bmp
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}