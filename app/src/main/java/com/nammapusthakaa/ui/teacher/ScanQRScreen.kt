package com.nammapusthakaa.ui.teacher

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.nammapusthakaa.ui.common.LoadingOverlay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executors

enum class BookAction { ISSUE, RETURN }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanQRScreen(
    teacherViewModel: TeacherViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var hasCameraPermission by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var currentAction by remember { mutableStateOf(BookAction.ISSUE) }
    var studentInput by remember { mutableStateOf("") }
    var hasLookedUp by remember { mutableStateOf(false) }
    val scanState = teacherViewModel.scanQRState

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            hasCameraPermission = true
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
        teacherViewModel.clearScanQRState()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Scan QR Code") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f).background(Color.Black)
                ) {
                    if (hasCameraPermission) {
                        QRScannerView(
                            onQrCodeScanned = { qrCode ->
                                teacherViewModel.processQRScan(qrCode)
                            }
                        )
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.White.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Camera permission is required to scan QR codes",
                                color = Color.White.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(32.dp)
                            )
                        }
                    }
                }

                if (scanState.bookInfo != null) {
                    BookFoundCard(
                        book = scanState.bookInfo,
                        onIssue = {
                            currentAction = BookAction.ISSUE
                            studentInput = ""
                            hasLookedUp = false
                            showDialog = true
                        },
                        onReturn = {
                            currentAction = BookAction.RETURN
                            studentInput = ""
                            hasLookedUp = false
                            showDialog = true
                        }
                    )
                }

                if (scanState.message != null && scanState.bookInfo == null) {
                    Text(
                        text = scanState.message,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    )
                }
            }
        }

        if (showDialog) {
            IssueReturnDialog(
                action = currentAction,
                bookTitle = scanState.bookInfo?.title ?: "",
                studentInfo = scanState.studentInfo,
                studentInput = studentInput,
                onStudentInputChange = {
                    studentInput = it
                    hasLookedUp = false
                },
                onLookupStudent = {
                    if (studentInput.isNotBlank()) {
                        teacherViewModel.lookupStudentForIssue(studentInput)
                        hasLookedUp = true
                    }
                },
                onConfirm = {
                    val studentId = scanState.selectedStudentId
                    val bookId = scanState.bookInfo?.id ?: return@IssueReturnDialog
                    if (currentAction == BookAction.ISSUE) {
                        teacherViewModel.issueBook(bookId, studentId)
                    } else {
                        teacherViewModel.returnBook(studentId, bookId)
                    }
                    showDialog = false
                    studentInput = ""
                    hasLookedUp = false
                },
                onDismiss = {
                    showDialog = false
                    studentInput = ""
                    hasLookedUp = false
                }
            )
        }

        LoadingOverlay(scanState.isProcessing)
    }
}

@Composable
private fun BookFoundCard(
    book: com.nammapusthakaa.data.local.entity.BookEntity,
    onIssue: () -> Unit,
    onReturn: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Book Found!", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Book, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(book.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(book.author, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Text("Category: ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(book.category, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
            }
            if (book.copies > 0) {
                Row {
                    Text("Copies: ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${book.copies} (${book.availableCopies} available)", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                }
            }
            Row {
                Text("Status: ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    if (book.availableCopies > 0) "Available" else "Currently Issued",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = if (book.availableCopies > 0) Color(0xFF10B981) else Color(0xFFEF4444)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onIssue,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    enabled = book.availableCopies > 0
                ) {
                    Text("Issue Book")
                }
                Button(
                    onClick = onReturn,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("Return Book")
                }
            }
        }
    }
}

@Composable
private fun IssueReturnDialog(
    action: BookAction,
    bookTitle: String,
    studentInfo: StudentInfo?,
    studentInput: String,
    onStudentInputChange: (String) -> Unit,
    onLookupStudent: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val title = if (action == BookAction.ISSUE) "Issue Book" else "Return Book"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
            ) {
                Text("Book: $bookTitle", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(12.dp))

                Text("Enter Student ID or Register Number:", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = studentInput,
                    onValueChange = onStudentInputChange,
                    label = { Text("Student ID / Reg No") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (studentInput.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onLookupStudent,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Look Up Student")
                    }
                }

                if (studentInfo != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Student Details:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    StudentDetailRow(icon = Icons.Default.Person, label = "Name", value = studentInfo.name)
                    StudentDetailRow(icon = Icons.Default.Person, label = "Reg No", value = studentInfo.registerNumber)
                    StudentDetailRow(icon = Icons.Default.School, label = "Class", value = studentInfo.className)
                    StudentDetailRow(icon = Icons.Default.Book, label = "Books Read", value = "${studentInfo.totalBooksRead}")
                    StudentDetailRow(icon = Icons.Default.Star, label = "Points", value = "${studentInfo.points}")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = studentInfo != null
            ) {
                Text(if (action == BookAction.ISSUE) "Confirm Issue" else "Confirm Return")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun StudentDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(6.dp))
        Text("$label: ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun QRScannerView(
    onQrCodeScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isScanning by remember { mutableStateOf(true) }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

                val barcodeScanner = BarcodeScanning.getClient()

                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(Size(1280, 720))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy: ImageProxy ->
                    @androidx.camera.core.ExperimentalGetImage
                    val mediaImage = imageProxy.image
                    if (mediaImage != null && isScanning) {
                        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                        barcodeScanner.process(image)
                            .addOnSuccessListener { barcodes ->
                                for (barcode in barcodes) {
                                    barcode.rawValue?.let { value ->
                                        if (isScanning) {
                                            isScanning = false
                                            onQrCodeScanned(value)
                                        }
                                    }
                                }
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }
                    } else {
                        imageProxy.close()
                    }
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}
