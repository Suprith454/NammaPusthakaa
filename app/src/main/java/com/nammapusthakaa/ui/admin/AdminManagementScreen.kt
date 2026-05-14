package com.nammapusthakaa.ui.admin

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nammapusthakaa.data.local.entity.BookEntity
import com.nammapusthakaa.data.local.entity.StudentEntity
import com.nammapusthakaa.data.local.entity.UserEntity
import com.nammapusthakaa.ui.common.EmptyState
import com.nammapusthakaa.ui.common.LoadingAnimation
import com.nammapusthakaa.ui.teacher.TransactionWithDetails
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminManagementScreen(
    adminViewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var deleteBookConfirm by remember { mutableStateOf<BookEntity?>(null) }
    var deleteTeacherConfirm by remember { mutableStateOf<UserEntity?>(null) }
    var deleteStudentConfirm by remember { mutableStateOf<StudentEntity?>(null) }
    var resetPwdTeacher by remember { mutableStateOf<UserEntity?>(null) }
    var newPassword by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        adminViewModel.loadBooks()
        adminViewModel.loadUsers()
        adminViewModel.loadHistory()
    }

    val tabs = listOf("Books", "Teachers", "Students", "History")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Database Management") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> BooksTab(adminViewModel, onDeleteBook = { deleteBookConfirm = it })
                1 -> TeachersTab(adminViewModel, onDeleteTeacher = { deleteTeacherConfirm = it }, onResetPassword = { resetPwdTeacher = it })
                2 -> StudentsTab(adminViewModel, onDeleteStudent = { deleteStudentConfirm = it })
                3 -> HistoryTab(adminViewModel)
            }
        }
    }

    if (deleteBookConfirm != null) {
        AlertDialog(
            onDismissRequest = { deleteBookConfirm = null },
            title = { Text("Delete Book") },
            text = { Text("Are you sure you want to permanently delete \"${deleteBookConfirm?.title}\"? This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    deleteBookConfirm?.let { adminViewModel.deleteBook(it) }
                    deleteBookConfirm = null
                }) { Text("Delete", color = Color(0xFFEF4444)) }
            },
            dismissButton = {
                TextButton(onClick = { deleteBookConfirm = null }) { Text("Cancel") }
            }
        )
    }

    if (deleteTeacherConfirm != null) {
        AlertDialog(
            onDismissRequest = { deleteTeacherConfirm = null },
            title = { Text("Remove Teacher") },
            text = { Text("Remove teacher \"${deleteTeacherConfirm?.name}\" (${deleteTeacherConfirm?.email}) from the system?") },
            confirmButton = {
                TextButton(onClick = {
                    deleteTeacherConfirm?.let { adminViewModel.deleteTeacher(it) }
                    deleteTeacherConfirm = null
                }) { Text("Remove", color = Color(0xFFEF4444)) }
            },
            dismissButton = {
                TextButton(onClick = { deleteTeacherConfirm = null }) { Text("Cancel") }
            }
        )
    }

    if (deleteStudentConfirm != null) {
        AlertDialog(
            onDismissRequest = { deleteStudentConfirm = null },
            title = { Text("Remove Student") },
            text = { Text("Remove student \"${deleteStudentConfirm?.name}\" (Reg: ${deleteStudentConfirm?.registerNumber}) from the system?") },
            confirmButton = {
                TextButton(onClick = {
                    deleteStudentConfirm?.let { adminViewModel.deleteStudent(it) }
                    deleteStudentConfirm = null
                }) { Text("Remove", color = Color(0xFFEF4444)) }
            },
            dismissButton = {
                TextButton(onClick = { deleteStudentConfirm = null }) { Text("Cancel") }
            }
        )
    }

    if (resetPwdTeacher != null) {
        AlertDialog(
            onDismissRequest = { resetPwdTeacher = null; newPassword = "" },
            title = { Text("Reset Password") },
            text = {
                Column {
                    Text("Reset password for ${resetPwdTeacher?.name} (${resetPwdTeacher?.email})")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password") },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newPassword.isNotBlank()) {
                        resetPwdTeacher?.let { adminViewModel.resetTeacherPassword(it, newPassword) }
                        resetPwdTeacher = null
                        newPassword = ""
                    }
                }) { Text("Reset") }
            },
            dismissButton = {
                TextButton(onClick = { resetPwdTeacher = null; newPassword = "" }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun BooksTab(adminViewModel: AdminViewModel, onDeleteBook: (BookEntity) -> Unit) {
    val state = adminViewModel.adminBooksState
    if (state.isLoading) {
        LoadingAnimation()
    } else if (state.books.isEmpty()) {
        EmptyState(message = "No books in the library")
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.books) { book ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Book, contentDescription = null, modifier = Modifier.size(36.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(book.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, maxLines = 1)
                            Text("${book.author} • ${book.category}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                text = if (book.availableCopies > 0) "Available" else "Issued",

                                color = if (book.availableCopies > 0) Color(0xFF10B981) else Color(0xFFEF4444)
                            )
                        }
                        IconButton(onClick = { onDeleteBook(book) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TeachersTab(adminViewModel: AdminViewModel, onDeleteTeacher: (UserEntity) -> Unit, onResetPassword: (UserEntity) -> Unit) {
    val state = adminViewModel.manageUsersState
    if (state.isLoading) {
        LoadingAnimation()
    } else if (state.teachers.isEmpty()) {
        EmptyState(message = "No teachers registered")
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.teachers) { teacher ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(36.dp), tint = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(teacher.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                            Text(teacher.email, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = { onResetPassword(teacher) }) {
                            Icon(Icons.Default.Lock, contentDescription = "Reset Password", tint = MaterialTheme.colorScheme.primary)
                        }
                        IconButton(onClick = { onDeleteTeacher(teacher) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFEF4444))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StudentsTab(adminViewModel: AdminViewModel, onDeleteStudent: (StudentEntity) -> Unit) {
    val state = adminViewModel.manageUsersState
    if (state.isLoading) {
        LoadingAnimation()
    } else if (state.students.isEmpty()) {
        EmptyState(message = "No students registered")
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.students) { student ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(36.dp), tint = MaterialTheme.colorScheme.tertiary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(student.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                            Text("Reg: ${student.registerNumber} • Class: ${student.className}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${student.totalBooksRead} books read • ${student.points} points", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = { onDeleteStudent(student) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFEF4444))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryTab(adminViewModel: AdminViewModel) {
    val state = adminViewModel.adminHistoryState
    val dateFormat = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())

    if (state.isLoading) {
        LoadingAnimation()
    } else if (state.transactions.isEmpty()) {
        EmptyState(message = "No transaction history")
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.transactions.forEach { txn ->
                item {
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
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (txn.isReturned) "Returned" else if (txn.isOverdue) "Overdue" else "Active",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (txn.isReturned) Color(0xFF10B981) else if (txn.isOverdue) Color(0xFFEF4444) else MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Book: ${txn.bookTitle}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                            if (txn.bookAuthor.isNotBlank()) {
                                Text("${txn.bookAuthor} • ${txn.bookCategory}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Student: ${txn.studentName} (${txn.studentRegisterNumber}, Class ${txn.studentClass})", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row {
                                Text("Issued: ", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(dateFormat.format(Date(txn.issueDate)), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Due: ", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(dateFormat.format(Date(txn.dueDate)), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
                                if (txn.returnedDate != null) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Returned: ", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(dateFormat.format(Date(txn.returnedDate)), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium, color = Color(0xFF10B981))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
