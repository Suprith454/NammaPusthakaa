package com.nammapusthakaa.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nammapusthakaa.ui.admin.AdminDashboardScreen
import com.nammapusthakaa.ui.admin.AdminManagementScreen
import com.nammapusthakaa.ui.admin.AdminViewModel
import com.nammapusthakaa.ui.auth.AuthViewModel
import com.nammapusthakaa.ui.auth.LoginScreen
import com.nammapusthakaa.ui.auth.SignupScreen
import com.nammapusthakaa.ui.common.OnboardingScreen
import com.nammapusthakaa.ui.common.SplashScreen
import com.nammapusthakaa.ui.student.BookCatalogScreen
import com.nammapusthakaa.ui.student.BookDetailScreen
import com.nammapusthakaa.ui.student.BorrowedBooksScreen
import com.nammapusthakaa.ui.student.LeaderboardScreen
import com.nammapusthakaa.ui.student.ReservedBooksScreen
import com.nammapusthakaa.ui.student.ReviewsScreen
import com.nammapusthakaa.ui.student.SearchScreen
import com.nammapusthakaa.ui.student.StudentMainScreen
import com.nammapusthakaa.ui.student.StudentProfileScreen
import com.nammapusthakaa.ui.student.StudentViewModel
import com.nammapusthakaa.ui.teacher.AddBookScreen
import com.nammapusthakaa.ui.teacher.ManageBooksScreen
import com.nammapusthakaa.ui.teacher.ReportsScreen
import com.nammapusthakaa.ui.teacher.ScanQRScreen
import com.nammapusthakaa.ui.teacher.StudentManagementScreen
import com.nammapusthakaa.ui.teacher.TeacherDashboardScreen
import com.nammapusthakaa.ui.teacher.TeacherViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    studentViewModel: StudentViewModel,
    teacherViewModel: TeacherViewModel,
    adminViewModel: AdminViewModel,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onGetStarted = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignup = { navController.navigate(Screen.Signup.route) },
                onLoginSuccess = { role, userId, userName ->
                    studentViewModel.initialize(userId, userName)
                    val destination = when (role) {
                        "Admin" -> Screen.AdminDashboard.route
                        "Teacher" -> Screen.TeacherDashboard.route
                        else -> Screen.StudentHome.route
                    }
                    navController.navigate(destination) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.popBackStack() },
                onSignupSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.StudentHome.route) {
            StudentMainScreen(
                studentViewModel = studentViewModel,
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToLeaderboard = { navController.navigate(Screen.Leaderboard.route) },
                onNavigateToBookDetail = { bookId -> navController.navigate(Screen.BookDetail.createRoute(bookId)) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.BookCatalog.route) {
            BookCatalogScreen(
                studentViewModel = studentViewModel,
                onNavigateToBookDetail = { bookId -> navController.navigate(Screen.BookDetail.createRoute(bookId)) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.BookDetail.route,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId") ?: 0
            BookDetailScreen(
                bookId = bookId,
                studentViewModel = studentViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                studentViewModel = studentViewModel,
                onNavigateToBookDetail = { bookId -> navController.navigate(Screen.BookDetail.createRoute(bookId)) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ReservedBooks.route) {
            ReservedBooksScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.BorrowedBooks.route) {
            BorrowedBooksScreen(
                studentViewModel = studentViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Reviews.route) {
            ReviewsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Leaderboard.route) {
            LeaderboardScreen(
                studentViewModel = studentViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.StudentProfile.route) {
            StudentProfileScreen(
                studentViewModel = studentViewModel,
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.TeacherDashboard.route) {
            TeacherDashboardScreen(
                teacherViewModel = teacherViewModel,
                onNavigateToAddBook = { navController.navigate(Screen.AddBook.route) },
                onNavigateToManageBooks = { navController.navigate(Screen.ManageBooks.route) },
                onNavigateToScanQR = { navController.navigate(Screen.ScanQR.route) },
                onNavigateToStudentManagement = { navController.navigate(Screen.StudentManagement.route) },
                onNavigateToReports = { navController.navigate(Screen.Reports.route) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AddBook.route) {
            AddBookScreen(
                teacherViewModel = teacherViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditBook.route,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId") ?: 0
            AddBookScreen(
                teacherViewModel = teacherViewModel,
                bookId = bookId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ManageBooks.route) {
            ManageBooksScreen(
                teacherViewModel = teacherViewModel,
                onNavigateToEditBook = { bookId -> navController.navigate(Screen.EditBook.createRoute(bookId)) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ScanQR.route) {
            ScanQRScreen(
                teacherViewModel = teacherViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.StudentManagement.route) {
            StudentManagementScreen(
                teacherViewModel = teacherViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Reports.route) {
            ReportsScreen(
                teacherViewModel = teacherViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(
                adminViewModel = adminViewModel,
                onNavigateToManagement = { navController.navigate(Screen.AdminManagement.route) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AdminManagement.route) {
            AdminManagementScreen(
                adminViewModel = adminViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
