package com.nammapusthakaa.ui.teacher

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nammapusthakaa.ui.common.EmptyState
import com.nammapusthakaa.ui.common.LoadingAnimation
import com.nammapusthakaa.ui.common.StatCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    teacherViewModel: TeacherViewModel,
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        teacherViewModel.loadReports()
    }

    val state = teacherViewModel.reportsState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reports & History") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(label = "Total", value = "${state.totalTransactions}", modifier = Modifier.weight(1f))
                        StatCard(label = "Active", value = "${state.activeIssuesCount}", modifier = Modifier.weight(1f))
                        StatCard(label = "Overdue", value = "${state.overdueCount}", modifier = Modifier.weight(1f))
                    }
                }

                if (state.transactions.isEmpty()) {
                    item {
                        EmptyState(message = "No transactions yet")
                    }
                } else {
                    item {
                        Text(
                            text = "Transaction History",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(state.transactions) { txn ->
                        TransactionCard(txn)
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionCard(txn: TransactionWithDetails) {
    val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (txn.isReturned) Icons.Default.CheckCircle else Icons.Default.Schedule,
                    contentDescription = null,
                    tint = if (txn.isReturned) Color(0xFF10B981) else if (txn.isOverdue) Color(0xFFEF4444) else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (txn.isReturned) "Returned" else if (txn.isOverdue) "Overdue" else "Active",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (txn.isReturned) Color(0xFF10B981) else if (txn.isOverdue) Color(0xFFEF4444) else MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Book, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(6.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(txn.bookTitle, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    if (txn.bookAuthor.isNotBlank()) {
                        Text("${txn.bookAuthor} • ${txn.bookCategory}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.width(6.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(txn.studentName, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                    Text(
                        "Reg: ${txn.studentRegisterNumber} • Class: ${txn.studentClass}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row {
                Text("Issued: ", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(dateFormat.format(Date(txn.issueDate)), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Due: ", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(dateFormat.format(Date(txn.dueDate)), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
                if (txn.returnedDate != null) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Returned: ", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(dateFormat.format(Date(txn.returnedDate)), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium, color = Color(0xFF10B981))
                }
            }
        }
    }
}
