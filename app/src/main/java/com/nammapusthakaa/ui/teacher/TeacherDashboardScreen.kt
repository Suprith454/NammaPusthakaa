package com.nammapusthakaa.ui.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nammapusthakaa.ui.common.LoadingAnimation
import com.nammapusthakaa.ui.common.StatCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboardScreen(
    teacherViewModel: TeacherViewModel,
    onNavigateToAddBook: () -> Unit,
    onNavigateToManageBooks: () -> Unit,
    onNavigateToScanQR: () -> Unit,
    onNavigateToStudentManagement: () -> Unit,
    onNavigateToReports: () -> Unit,
    onLogout: () -> Unit
) {
    LaunchedEffect(Unit) {
        teacherViewModel.loadDashboard()
    }

    val state = teacherViewModel.dashboardState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Teacher Dashboard") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        if (state.isLoading) {
            LoadingAnimation()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Library Overview",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Manage your school library efficiently",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            label = "Total Books",
                            value = "${state.totalBooks}",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "Issued",
                            value = "${state.issuedBooks}",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            label = "Overdue",
                            value = "${state.overdueBooks}",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "Available",
                            value = "${state.availableBooks}",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Text(
                        text = "Quick Actions",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ActionCard(
                            icon = Icons.Default.Add,
                            label = "Add Book",
                            color = MaterialTheme.colorScheme.primary,
                            onClick = onNavigateToAddBook,
                            modifier = Modifier.weight(1f)
                        )
                        ActionCard(
                            icon = Icons.Default.MenuBook,
                            label = "Manage Books",
                            color = MaterialTheme.colorScheme.secondary,
                            onClick = onNavigateToManageBooks,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ActionCard(
                            icon = Icons.Default.QrCodeScanner,
                            label = "Scan QR",
                            color = MaterialTheme.colorScheme.tertiary,
                            onClick = onNavigateToScanQR,
                            modifier = Modifier.weight(1f)
                        )
                        ActionCard(
                            icon = Icons.Default.ManageAccounts,
                            label = "Students",
                            color = MaterialTheme.colorScheme.error,
                            onClick = onNavigateToStudentManagement,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    ActionCard(
                        icon = Icons.Default.Assessment,
                        label = "Reports & Analytics",
                        color = MaterialTheme.colorScheme.primary,
                        onClick = onNavigateToReports,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (state.recentTransactions.isNotEmpty()) {
                    item {
                        Text(
                            text = "Recent Activity",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    state.recentTransactions.forEach { txn ->
                        item {
                            val dateFormat = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Book, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(txn.bookTitle, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                                        Text(
                                            if (txn.isReturned) "Returned" else "Issued",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (txn.isReturned) Color(0xFF10B981) else Color(0xFF3B82F6)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            "${txn.studentName} (${txn.studentRegisterNumber}, Class ${txn.studentClass})",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Text(
                                        text = dateFormat.format(Date(txn.issueDate)),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
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

@Composable
fun ActionCard(
    icon: ImageVector,
    label: String,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
