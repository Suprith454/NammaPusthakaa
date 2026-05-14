package com.nammapusthakaa.ui.student

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nammapusthakaa.ui.common.BookCard
import com.nammapusthakaa.ui.common.CategoryChip
import com.nammapusthakaa.ui.common.SectionHeader
import com.nammapusthakaa.ui.common.StatCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentHomeScreen(
    studentViewModel: StudentViewModel,
    onNavigateToCatalog: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToBorrowed: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToBookDetail: (Int) -> Unit,
    onLogout: () -> Unit
) {
    LaunchedEffect(Unit) {
        studentViewModel.loadHomeData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Namma Pustaka") },
                actions = {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
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
        StudentHomeContent(
            studentViewModel = studentViewModel,
            onNavigateToCatalog = onNavigateToCatalog,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToBorrowed = onNavigateToBorrowed,
            onNavigateToLeaderboard = onNavigateToLeaderboard,
            onNavigateToProfile = onNavigateToProfile,
            onNavigateToBookDetail = onNavigateToBookDetail,
            onLogout = onLogout,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun StudentHomeContent(
    studentViewModel: StudentViewModel,
    onNavigateToCatalog: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToBorrowed: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToBookDetail: (Int) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = studentViewModel.homeState

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
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
                        text = "Hello, ${state.studentName}!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Keep reading and exploring",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatCard(
                            label = "Books Read",
                            value = "${state.totalBooksRead}",
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        StatCard(
                            label = "Points",
                            value = "${state.points}",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        item {
            SectionHeader(
                title = "Categories",
                action = {
                    Text(
                        text = "See All",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable(onClick = onNavigateToCatalog)
                    )
                }
            )
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.categories) { category ->
                    CategoryChip(
                        text = category,
                        isSelected = false,
                        onClick = {
                            studentViewModel.filterByCategory(category)
                            onNavigateToCatalog()
                        }
                    )
                }
            }
        }

        item {
            SectionHeader(
                title = "Recently Added",
                action = {
                    Text(
                        text = "See All",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable(onClick = onNavigateToCatalog)
                    )
                }
            )
        }

        items(state.recentBooks.take(5)) { book ->
            BookCard(
                title = book.title,
                author = book.author,
                category = book.category,
                rating = 4.0f,
                isAvailable = book.availableCopies > 0,
                onClick = { onNavigateToBookDetail(book.id) }
            )
        }

        item {
            SectionHeader(title = "Quick Actions")
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    icon = Icons.Default.LibraryBooks,
                    label = "My Books",
                    onClick = onNavigateToBorrowed,
                    modifier = Modifier.weight(1f)
                )
                QuickActionCard(
                    icon = Icons.Default.EmojiEvents,
                    label = "Leaderboard",
                    onClick = onNavigateToLeaderboard,
                    modifier = Modifier.weight(1f)
                )
                QuickActionCard(
                    icon = Icons.Default.Person,
                    label = "Profile",
                    onClick = onNavigateToProfile,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
fun QuickActionCard(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
