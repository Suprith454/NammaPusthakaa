package com.nammapusthakaa.ui.student

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class BottomNavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem("Catalog", Icons.Filled.Book, Icons.Outlined.Book),
    BottomNavItem("Borrowed", Icons.Filled.LibraryBooks, Icons.Outlined.LibraryBooks),
    BottomNavItem("Profile", Icons.Filled.Person, Icons.Outlined.Person)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentMainScreen(
    studentViewModel: StudentViewModel,
    onNavigateToSearch: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    onNavigateToBookDetail: (Int) -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        studentViewModel.loadHomeData()
    }

    LaunchedEffect(selectedTab) {
        when (selectedTab) {
            1 -> studentViewModel.loadCatalog()
            2 -> studentViewModel.loadBorrowedBooks()
            3 -> studentViewModel.loadProfile()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(bottomNavItems[selectedTab].label) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == index) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> StudentHomeContent(
                studentViewModel = studentViewModel,
                onNavigateToCatalog = { selectedTab = 1 },
                onNavigateToSearch = onNavigateToSearch,
                onNavigateToBorrowed = { selectedTab = 2 },
                onNavigateToLeaderboard = onNavigateToLeaderboard,
                onNavigateToProfile = { selectedTab = 3 },
                onNavigateToBookDetail = onNavigateToBookDetail,
                onLogout = onLogout,
                modifier = Modifier.padding(padding)
            )
            1 -> BookCatalogContent(
                studentViewModel = studentViewModel,
                onNavigateToBookDetail = onNavigateToBookDetail,
                modifier = Modifier.padding(padding)
            )
            2 -> BorrowedBooksContent(
                studentViewModel = studentViewModel,
                modifier = Modifier.padding(padding)
            )
            3 -> StudentProfileContent(
                studentViewModel = studentViewModel,
                onLogout = onLogout,
                modifier = Modifier.padding(padding)
            )
        }
    }
}
