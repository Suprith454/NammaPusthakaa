package com.nammapusthakaa.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Onboarding : Screen("onboarding")
    data object Login : Screen("login")
    data object Signup : Screen("signup")

    data object StudentHome : Screen("student_home")
    data object BookCatalog : Screen("book_catalog")
    data object BookDetail : Screen("book_detail/{bookId}") {
        fun createRoute(bookId: Int) = "book_detail/$bookId"
    }
    data object Search : Screen("search")
    data object ReservedBooks : Screen("reserved_books")
    data object BorrowedBooks : Screen("borrowed_books")
    data object Reviews : Screen("reviews")
    data object Leaderboard : Screen("leaderboard")
    data object StudentProfile : Screen("student_profile")

    data object TeacherDashboard : Screen("teacher_dashboard")
    data object AddBook : Screen("add_book")
    data object EditBook : Screen("edit_book/{bookId}") {
        fun createRoute(bookId: Int) = "edit_book/$bookId"
    }
    data object ManageBooks : Screen("manage_books")
    data object ScanQR : Screen("scan_qr")
    data object StudentManagement : Screen("student_management")
    data object Reports : Screen("reports")

    data object AdminDashboard : Screen("admin_dashboard")
    data object AdminManagement : Screen("admin_management")
}
