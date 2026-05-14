package com.nammapusthakaa.ui.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nammapusthakaa.data.local.entity.BookEntity
import com.nammapusthakaa.ui.common.EmptyState
import com.nammapusthakaa.ui.common.LoadingAnimation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageBooksScreen(
    teacherViewModel: TeacherViewModel,
    onNavigateToEditBook: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        teacherViewModel.loadManageBooks()
    }

    val state = teacherViewModel.manageBooksState
    var deleteConfirmBook by remember { mutableStateOf<BookEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Books") },
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
        if (state.isLoading) {
            LoadingAnimation()
        } else if (state.books.isEmpty()) {
            EmptyState(message = "No books in the library")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.books) { book ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Book,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = book.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${book.author} • ${book.category}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = if (book.availableCopies > 0) "Available" else "Issued",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (book.availableCopies > 0) Color(0xFF10B981) else Color(0xFFEF4444)
                                )
                            }
                            IconButton(onClick = { onNavigateToEditBook(book.id) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                            }
                            IconButton(onClick = { deleteConfirmBook = book }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444))
                            }
                        }
                    }
                }
            }
        }
    }

    if (deleteConfirmBook != null) {
        AlertDialog(
            onDismissRequest = { deleteConfirmBook = null },
            title = { Text("Delete Book") },
            text = { Text("Are you sure you want to delete \"${deleteConfirmBook?.title}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    deleteConfirmBook?.let { teacherViewModel.deleteBook(it) }
                    deleteConfirmBook = null
                }) {
                    Text("Delete", color = Color(0xFFEF4444))
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteConfirmBook = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
