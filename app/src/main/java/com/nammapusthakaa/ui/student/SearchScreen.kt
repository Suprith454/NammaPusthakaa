package com.nammapusthakaa.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nammapusthakaa.ui.common.BookCard
import com.nammapusthakaa.ui.common.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    studentViewModel: StudentViewModel,
    onNavigateToBookDetail: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state = studentViewModel.searchState

    LaunchedEffect(Unit) {
        studentViewModel.searchBooks("")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Books") },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                OutlinedTextField(
                    value = state.query,
                    onValueChange = { studentViewModel.searchBooks(it) },
                    placeholder = { Text("Search by title or author...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            if (state.results.isEmpty() && state.query.isNotEmpty()) {
                item { EmptyState(message = "No books found for \"${state.query}\"") }
            } else if (state.query.isEmpty()) {
                item { EmptyState(message = "Search for your favorite books") }
            } else {
                items(state.results) { book ->
                    BookCard(
                        title = book.title,
                        author = book.author,
                        category = book.category,
                        rating = 4.0f,
                        isAvailable = book.availableCopies > 0,
                        onClick = { onNavigateToBookDetail(book.id) }
                    )
                }
            }
        }
    }
}
