package com.nammapusthakaa.ui.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammapusthakaa.data.local.entity.BookEntity
import com.nammapusthakaa.data.local.entity.StudentEntity
import com.nammapusthakaa.data.local.entity.TransactionEntity
import com.nammapusthakaa.data.repository.BookRepository
import com.nammapusthakaa.data.repository.StudentRepository
import com.nammapusthakaa.data.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class TeacherDashboardState(
    val totalBooks: Int = 0,
    val issuedBooks: Int = 0,
    val overdueBooks: Int = 0,
    val availableBooks: Int = 0,
    val recentTransactions: List<TransactionWithDetails> = emptyList(),
    val isLoading: Boolean = false
)

data class AddBookState(
    val title: String = "",
    val author: String = "",
    val category: String = "General",
    val language: String = "Kannada",
    val copies: String = "",
    val description: String = "",
    val kannadaSummary: String = "",
    val coverUrl: String = "",
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val success: String? = null
)

data class ManageBooksState(
    val books: List<BookEntity> = emptyList(),
    val isLoading: Boolean = false
)

data class ReportsState(
    val totalTransactions: Int = 0,
    val activeIssuesCount: Int = 0,
    val overdueCount: Int = 0,
    val transactions: List<TransactionWithDetails> = emptyList(),
    val isLoading: Boolean = false
)

data class TransactionWithDetails(
    val id: Int,
    val bookTitle: String,
    val bookAuthor: String = "",
    val bookCategory: String = "",
    val studentName: String,
    val studentRegisterNumber: String = "",
    val studentClass: String = "",
    val issueDate: Long,
    val dueDate: Long,
    val returnedDate: Long? = null,
    val isReturned: Boolean = false,
    val isOverdue: Boolean = false
)

data class ScanQRState(
    val scannedCode: String = "",
    val bookInfo: BookEntity? = null,
    val studentInfo: StudentInfo? = null,
    val selectedStudentId: Int = 0,
    val isProcessing: Boolean = false,
    val message: String? = null
)

data class StudentInfo(
    val name: String,
    val registerNumber: String,
    val className: String,
    val totalBooksRead: Int,
    val points: Int
)

class TeacherViewModel(
    private val bookRepository: BookRepository,
    private val transactionRepository: TransactionRepository,
    private val studentRepository: StudentRepository
) : ViewModel() {

    var dashboardState by mutableStateOf(TeacherDashboardState())
        private set
    var addBookState by mutableStateOf(AddBookState())
        private set
    var manageBooksState by mutableStateOf(ManageBooksState())
        private set
    var scanQRState by mutableStateOf(ScanQRState())
        private set
    var reportsState by mutableStateOf(ReportsState())
        private set

    fun loadDashboard() {
        viewModelScope.launch {
            dashboardState = dashboardState.copy(isLoading = true)
            try {
                val count = bookRepository.getBookCount().first()
                dashboardState = dashboardState.copy(totalBooks = count)
            } catch (_: Exception) {}
            try {
                val count = transactionRepository.getActiveIssuesCount().first()
                dashboardState = dashboardState.copy(issuedBooks = count)
            } catch (_: Exception) {}
            try {
                val count = transactionRepository.getOverdueCount(System.currentTimeMillis()).first()
                dashboardState = dashboardState.copy(overdueBooks = count)
            } catch (_: Exception) {}
            try {
                val count = bookRepository.getAvailableBookCount().first()
                dashboardState = dashboardState.copy(availableBooks = count)
            } catch (_: Exception) {}

            try {
                val allTransactions = transactionRepository.getAllTransactions().first()
                val books = bookRepository.getAllBooks().first()
                val students = studentRepository.getAllStudents().first()
                val recent = allTransactions.sortedByDescending { it.issueDate }.take(5).map { txn ->
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
                        isReturned = txn.isReturned
                    )
                }
                dashboardState = dashboardState.copy(recentTransactions = recent)
            } catch (_: Exception) {}

            dashboardState = dashboardState.copy(isLoading = false)
        }
    }

    fun loadManageBooks() {
        viewModelScope.launch {
            manageBooksState = manageBooksState.copy(isLoading = true)
            try {
                val books = bookRepository.getAllBooks().first()
                manageBooksState = manageBooksState.copy(books = books, isLoading = false)
            } catch (_: Exception) {
                manageBooksState = manageBooksState.copy(isLoading = false)
            }
        }
    }

    fun loadReports() {
        viewModelScope.launch {
            reportsState = reportsState.copy(isLoading = true)
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

                val active = transactionRepository.getActiveIssuesCount().first()
                val overdue = transactionRepository.getOverdueCount(System.currentTimeMillis()).first()

                reportsState = ReportsState(
                    totalTransactions = allTransactions.size,
                    activeIssuesCount = active,
                    overdueCount = overdue,
                    transactions = details,
                    isLoading = false
                )
            } catch (_: Exception) {
                reportsState = reportsState.copy(isLoading = false)
            }
        }
    }

    fun updateAddBookField(field: String, value: String) {
        addBookState = when (field) {
            "title" -> addBookState.copy(title = value)
            "author" -> addBookState.copy(author = value)
            "category" -> addBookState.copy(category = value)
            "language" -> addBookState.copy(language = value)
            "copies" -> addBookState.copy(copies = value)
            "description" -> addBookState.copy(description = value)
            "kannadaSummary" -> addBookState.copy(kannadaSummary = value)
            "coverUrl" -> addBookState.copy(coverUrl = value)
            else -> addBookState
        }
    }

    fun loadBookForEdit(bookId: Int) {
        viewModelScope.launch {
            val book = bookRepository.getBookById(bookId)
            if (book != null) {
                addBookState = AddBookState(
                    title = book.title,
                    author = book.author,
                    category = book.category,
                    language = book.language,
                    copies = book.copies.toString(),
                    description = book.description,
                    kannadaSummary = book.kannadaSummary,
                    coverUrl = book.coverUrl,
                    isEditing = true
                )
            }
        }
    }

    fun saveBook(bookId: Int? = null) {
        viewModelScope.launch {
            addBookState = addBookState.copy(isSaving = true, error = null, success = null)
            try {
                if (addBookState.title.isBlank()) {
                    addBookState = addBookState.copy(isSaving = false, error = "Title is required")
                    return@launch
                }
                if (addBookState.author.isBlank()) {
                    addBookState = addBookState.copy(isSaving = false, error = "Author is required")
                    return@launch
                }

                val copies = addBookState.copies.toIntOrNull() ?: 1

                if (addBookState.isEditing && bookId != null) {
                    val existing = bookRepository.getBookById(bookId)
                    if (existing != null) {
                        bookRepository.updateBook(
                            existing.copy(
                                title = addBookState.title,
                                author = addBookState.author,
                                category = addBookState.category,
                                language = addBookState.language,
                                copies = copies,
                                description = addBookState.description,
                                kannadaSummary = addBookState.kannadaSummary,
                                coverUrl = addBookState.coverUrl
                            )
                        )
                        addBookState = addBookState.copy(isSaving = false, success = "Book updated successfully")
                    }
                } else {
                    bookRepository.insertBook(
                        BookEntity(
                            title = addBookState.title,
                            author = addBookState.author,
                            category = addBookState.category,
                            language = addBookState.language,
                            copies = copies,
                            availableCopies = copies,
                            qrCode = "NPL-${System.currentTimeMillis()}",
                            description = addBookState.description,
                            kannadaSummary = addBookState.kannadaSummary,
                            coverUrl = addBookState.coverUrl
                        )
                    )
                    addBookState = addBookState.copy(isSaving = false, success = "Book added successfully")
                }
            } catch (e: Exception) {
                addBookState = addBookState.copy(isSaving = false, error = "Failed to save book: ${e.message}")
            }
        }
    }

    fun deleteBook(book: BookEntity) {
        viewModelScope.launch {
            try {
                bookRepository.deleteBook(book)
            } catch (_: Exception) {}
        }
    }

    suspend fun studentRepositoryGetAll(): List<StudentEntity> = studentRepository.getAllStudents().first()

    fun resetAddBookState() {
        addBookState = AddBookState()
    }

    fun processQRScan(qrCode: String) {
        viewModelScope.launch {
            scanQRState = scanQRState.copy(scannedCode = qrCode, isProcessing = true)
            try {
                val books = bookRepository.getAllBooks().first()
                val book = books.find { it.qrCode == qrCode }
                scanQRState = scanQRState.copy(
                    bookInfo = book,
                    isProcessing = false,
                    message = if (book != null) "Book found: ${book.title}" else "No book found with this QR code"
                )
            } catch (e: Exception) {
                scanQRState = scanQRState.copy(
                    isProcessing = false,
                    message = "Error: ${e.message}"
                )
            }
        }
    }

    fun lookupStudentForIssue(studentIdOrReg: String) {
        viewModelScope.launch {
            scanQRState = scanQRState.copy(isProcessing = true, studentInfo = null)
            try {
                val student = studentIdOrReg.toIntOrNull()?.let { studentRepository.getStudentById(it) }
                    ?: studentRepository.getStudentByRegisterNumber(studentIdOrReg)
                if (student != null) {
                    scanQRState = scanQRState.copy(
                        studentInfo = StudentInfo(
                            name = student.name,
                            registerNumber = student.registerNumber,
                            className = student.className,
                            totalBooksRead = student.totalBooksRead,
                            points = student.points
                        ),
                        selectedStudentId = student.id,
                        isProcessing = false
                    )
                } else {
                    scanQRState = scanQRState.copy(isProcessing = false, message = "No student found with that ID")
                }
            } catch (e: Exception) {
                scanQRState = scanQRState.copy(isProcessing = false, message = "Error: ${e.message}")
            }
        }
    }

    fun issueBook(bookId: Int, studentId: Int) {
        viewModelScope.launch {
            try {
                val dueDate = System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000L
                val student = studentRepository.getStudentById(studentId)
                val book = bookRepository.getBookById(bookId)

                transactionRepository.insertTransaction(
                    TransactionEntity(
                        bookId = bookId,
                        studentId = studentId,
                        studentName = student?.name ?: "",
                        bookTitle = book?.title ?: "",
                        dueDate = dueDate
                    )
                )

                if (book != null && book.availableCopies > 0) {
                    bookRepository.updateBook(book.copy(availableCopies = book.availableCopies - 1))
                }

                scanQRState = scanQRState.copy(
                    studentInfo = null,
                    bookInfo = null,
                    message = "Book issued to ${student?.name ?: studentId}! Due: ${java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date(dueDate))}"
                )
            } catch (e: Exception) {
                scanQRState = scanQRState.copy(message = "Failed to issue book: ${e.message}")
            }
        }
    }

    fun returnBook(studentId: Int, bookId: Int) {
        viewModelScope.launch {
            try {
                val allTransactions = transactionRepository.getAllTransactions().first()
                val active = allTransactions.find { it.bookId == bookId && it.studentId == studentId && !it.isReturned }
                if (active != null) {
                    transactionRepository.updateTransaction(
                        active.copy(
                            isReturned = true,
                            returnedDate = System.currentTimeMillis()
                        )
                    )
                }

                val book = bookRepository.getBookById(bookId)
                if (book != null) {
                    bookRepository.updateBook(book.copy(availableCopies = book.availableCopies + 1))
                    val student = studentRepository.getStudentById(studentId)
                    if (student != null) {
                        studentRepository.updateStudent(
                            student.copy(totalBooksRead = student.totalBooksRead + 1, points = student.points + 10)
                        )
                    }
                }

                scanQRState = scanQRState.copy(
                    studentInfo = null,
                    bookInfo = null,
                    message = "Book returned successfully!"
                )
            } catch (e: Exception) {
                scanQRState = scanQRState.copy(message = "Failed to return book: ${e.message}")
            }
        }
    }

    fun clearScanQRState() {
        scanQRState = ScanQRState()
    }
}
