package com.nammapusthakaa.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nammapusthakaa.ui.common.BookCard
import com.nammapusthakaa.ui.common.CategoryChip
import com.nammapusthakaa.ui.common.EmptyState
import com.nammapusthakaa.ui.common.LoadingAnimation
import com.nammapusthakaa.ui.common.SectionHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookCatalogScreen(
    studentViewModel: StudentViewModel,
    onNavigateToBookDetail: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        studentViewModel.loadCatalog()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Catalog") },
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
        BookCatalogContent(
            studentViewModel = studentViewModel,
            onNavigateToBookDetail = onNavigateToBookDetail,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun BookCatalogContent(
    studentViewModel: StudentViewModel,
    onNavigateToBookDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = studentViewModel.catalogState
    val categories = studentViewModel.homeState.categories

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionHeader(title = "Browse by Category")
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    CategoryChip(
                        text = "All",
                        isSelected = state.selectedCategory == null,
                        onClick = { studentViewModel.filterByCategory(null) }
                    )
                }
                items(categories) { category ->
                    CategoryChip(
                        text = category,
                        isSelected = state.selectedCategory == category,
                        onClick = { studentViewModel.filterByCategory(category) }
                    )
                }
            }
        }

        item {
            SectionHeader(title = "Books")
        }

        if (state.isLoading) {
            item { LoadingAnimation() }
        } else if (state.books.isEmpty()) {
            item { EmptyState(message = "No books found") }
        } else {
            var index = 0
            val books = state.books
            while (index < books.size) {
                val first = books[index]
                val second = if (index + 1 < books.size) books[index + 1] else null
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        BookCard(
                            title = first.title,
                            author = first.author,
                            category = first.category,
                            rating = 4.0f,
                            isAvailable = first.availableCopies > 0,
                            onClick = { onNavigateToBookDetail(first.id) },
                            modifier = Modifier.weight(1f)
                        )
                        if (second != null) {
                            BookCard(
                                title = second.title,
                                author = second.author,
                                category = second.category,
                                rating = 4.0f,
                                isAvailable = second.availableCopies > 0,
                                onClick = { onNavigateToBookDetail(second.id) },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
                index += 2
            }
        }

        item { Spacer(modifier = Modifier.padding(bottom = 16.dp)) }
    }
}
