package com.nammapusthakaa.ui.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammapusthakaa.data.local.entity.BookEntity
import com.nammapusthakaa.data.local.entity.UserEntity
import com.nammapusthakaa.data.local.entity.StudentEntity
import com.nammapusthakaa.data.repository.BookRepository
import com.nammapusthakaa.data.repository.StudentRepository
import com.nammapusthakaa.data.repository.TransactionRepository
import com.nammapusthakaa.data.repository.UserRepository
import com.nammapusthakaa.ui.teacher.TransactionWithDetails
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class AdminDashboardState(
    val totalUsers: Int = 0,
    val totalBooks: Int = 0,
    val totalTransactions: Int = 0,
    val totalStudents: Int = 0,
    val isLoading: Boolean = false
)

data class AdminHistoryState(
    val transactions: List<TransactionWithDetails> = emptyList(),
    val isLoading: Boolean = false
)

data class ManageUsersState(
    val teachers: List<UserEntity> = emptyList(),
    val students: List<StudentEntity> = emptyList(),
    val isLoading: Boolean = false
)

data class AdminBooksState(
    val books: List<BookEntity> = emptyList(),
    val isLoading: Boolean = false
)

class AdminViewModel(
    private val bookRepository: BookRepository,
    private val studentRepository: StudentRepository,
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var adminDashboardState by mutableStateOf(AdminDashboardState())
        private set
    var manageUsersState by mutableStateOf(ManageUsersState())
        private set
    var adminBooksState by mutableStateOf(AdminBooksState())
        private set
    var adminHistoryState by mutableStateOf(AdminHistoryState())
        private set

    fun loadDashboard() {
        viewModelScope.launch {
            adminDashboardState = adminDashboardState.copy(isLoading = true)
            try { adminDashboardState = adminDashboardState.copy(totalBooks = bookRepository.getBookCount().first()) } catch (_: Exception) {}
            try { adminDashboardState = adminDashboardState.copy(totalUsers = userRepository.getUserCount().first()) } catch (_: Exception) {}
            try { adminDashboardState = adminDashboardState.copy(totalTransactions = transactionRepository.getTotalTransactionsCount().first()) } catch (_: Exception) {}
            try { adminDashboardState = adminDashboardState.copy(totalStudents = studentRepository.getStudentCount().first()) } catch (_: Exception) {}
            adminDashboardState = adminDashboardState.copy(isLoading = false)
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            manageUsersState = manageUsersState.copy(isLoading = true)
            try {
                manageUsersState = manageUsersState.copy(
                    teachers = userRepository.getAllUsers().first().filter { it.role == "Teacher" },
                    students = studentRepository.getAllStudents().first(),
                    isLoading = false
                )
            } catch (_: Exception) {
                manageUsersState = manageUsersState.copy(isLoading = false)
            }
        }
    }

    fun loadBooks() {
        viewModelScope.launch {
            adminBooksState = adminBooksState.copy(isLoading = true)
            try {
                adminBooksState = adminBooksState.copy(books = bookRepository.getAllBooks().first(), isLoading = false)
            } catch (_: Exception) {
                adminBooksState = adminBooksState.copy(isLoading = false)
            }
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            adminHistoryState = adminHistoryState.copy(isLoading = true)
            try {
                val allTransactions = transactionRepository.getAllTransactions().first()
                val books = bookRepository.getAllBooks().first()
                val students = studentRepository.getAllStudents().first()
                val details = allTransactions.map { txn ->
                    val book = books.find { it.id == txn.bookId }
                    val student = students.find { it.id == txn.studentId }
                    TransactionWithDetails(
                        id = txn.id,
                        bookTitle = book?.title ?: txn.bookTitle,
                        bookAuthor = book?.author ?: "",
                        bookCategory = book?.category ?: "",
                        studentName = student?.name ?: txn.studentName,
                        studentRegisterNumber = student?.registerNumber ?: "",
                        studentClass = student?.className ?: "",
                        issueDate = txn.issueDate,
                        dueDate = txn.dueDate,
                        returnedDate = txn.returnedDate,
                        isReturned = txn.isReturned,
                        isOverdue = txn.isOverdue
                    )
                }.sortedByDescending { it.issueDate }
                adminHistoryState = AdminHistoryState(transactions = details, isLoading = false)
            } catch (_: Exception) {
                adminHistoryState = adminHistoryState.copy(isLoading = false)
            }
        }
    }

    fun deleteBook(book: BookEntity) {
        viewModelScope.launch {
            try {
                transactionRepository.deleteTransactionsByBook(book.id)
                bookRepository.deleteBook(book)
                loadBooks()
                loadDashboard()
            } catch (_: Exception) {}
        }
    }

    fun deleteTeacher(user: UserEntity) {
        viewModelScope.launch {
            try {
                userRepository.deleteUser(user)
                loadUsers()
                loadDashboard()
            } catch (_: Exception) {}
        }
    }

    fun deleteStudent(student: StudentEntity) {
        viewModelScope.launch {
            try {
                transactionRepository.deleteTransactionsByStudent(student.id)
                studentRepository.deleteStudent(student)
                loadUsers()
                loadDashboard()
            } catch (_: Exception) {}
        }
    }

    fun resetTeacherPassword(user: UserEntity, newPassword: String) {
        viewModelScope.launch {
            try {
                userRepository.updateUser(user.copy(password = newPassword))
                loadUsers()
            } catch (_: Exception) {}
        }
    }
}
