package com.nammapusthakaa.ui.student

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
import com.nammapusthakaa.data.repository.ReviewRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class StudentHomeState(
    val studentName: String = "Student",
    val totalBooksRead: Int = 0,
    val points: Int = 0,
    val recentBooks: List<BookEntity> = emptyList(),
    val categories: List<String> = emptyList(),
    val leaderboard: List<StudentEntity> = emptyList(),
    val isLoading: Boolean = false
)

data class CatalogState(
    val books: List<BookEntity> = emptyList(),
    val selectedCategory: String? = null,
    val isLoading: Boolean = false
)

data class BookDetailState(
    val book: BookEntity? = null,
    val averageRating: Float = 0f,
    val reviewCount: Int = 0,
    val isBorrowed: Boolean = false,
    val isLoading: Boolean = false
)

data class SearchState(
    val query: String = "",
    val results: List<BookEntity> = emptyList()
)

data class BorrowedState(
    val borrowedBooks: List<TransactionEntity> = emptyList(),
    val isLoading: Boolean = false
)

data class ProfileState(
    val student: StudentEntity? = null,
    val isLoading: Boolean = false
)

class StudentViewModel(
    private val bookRepository: BookRepository,
    private val studentRepository: StudentRepository,
    private val transactionRepository: TransactionRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    var homeState by mutableStateOf(StudentHomeState())
        private set
    var catalogState by mutableStateOf(CatalogState())
        private set
    var bookDetailState by mutableStateOf(BookDetailState())
        private set
    var searchState by mutableStateOf(SearchState())
        private set
    var borrowedState by mutableStateOf(BorrowedState())
        private set
    var profileState by mutableStateOf(ProfileState())
        private set

    var snackbarMessage by mutableStateOf<String?>(null)
        private set

    fun showSnackbar(msg: String) { snackbarMessage = msg }
    fun clearSnackbar() { snackbarMessage = null }

    private var currentStudentId: Int = 0

    fun initialize(studentId: Int, studentName: String) {
        currentStudentId = studentId
        homeState = homeState.copy(studentName = studentName)
        loadHomeData()
        loadCatalog()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            homeState = homeState.copy(isLoading = true)
            try {
                val books = bookRepository.getRecentBooks(10).first()
                homeState = homeState.copy(recentBooks = books)
            } catch (_: Exception) {}
            try {
                val cats = bookRepository.getAllCategories().first()
                homeState = homeState.copy(categories = cats.take(8))
            } catch (_: Exception) {}
            try {
                val list = studentRepository.getLeaderboard().first()
                homeState = homeState.copy(leaderboard = list.take(5))
            } catch (_: Exception) {}
            try {
                val student = studentRepository.getStudentById(currentStudentId)
                if (student != null) {
                    homeState = homeState.copy(
                        totalBooksRead = student.totalBooksRead,
                        points = student.points
                    )
                }
            } catch (_: Exception) {}
            homeState = homeState.copy(isLoading = false)
        }
    }

    fun loadCatalog() {
        viewModelScope.launch {
            catalogState = catalogState.copy(isLoading = true)
            try {
                val books = bookRepository.getAllBooks().first()
                catalogState = catalogState.copy(
                    books = if (catalogState.selectedCategory != null) {
                        books.filter { it.category == catalogState.selectedCategory }
                    } else books,
                    isLoading = false
                )
            } catch (_: Exception) {
                catalogState = catalogState.copy(isLoading = false)
            }
        }
    }

    fun filterByCategory(category: String?) {
        catalogState = catalogState.copy(selectedCategory = category)
        viewModelScope.launch {
            val books = if (category == null) {
                bookRepository.getAllBooks().first()
            } else {
                bookRepository.getBooksByCategory(category).first()
            }
            catalogState = catalogState.copy(books = books)
        }
    }

    fun loadBookDetail(bookId: Int) {
        viewModelScope.launch {
            bookDetailState = bookDetailState.copy(isLoading = true)
            try {
                val book = bookRepository.getBookById(bookId)
                bookDetailState = bookDetailState.copy(book = book)
            } catch (_: Exception) {}
            try {
                val rating = reviewRepository.getAverageRatingForBook(bookId).first()
                bookDetailState = bookDetailState.copy(averageRating = rating ?: 0f)
            } catch (_: Exception) {}
            try {
                val count = reviewRepository.getReviewCountForBook(bookId).first()
                bookDetailState = bookDetailState.copy(reviewCount = count)
            } catch (_: Exception) {}
            try {
                val transactions = transactionRepository.getBorrowedBooksByStudent(currentStudentId).first()
                bookDetailState = bookDetailState.copy(
                    isBorrowed = transactions.any { it.bookId == bookId && !it.isReturned }
                )
            } catch (_: Exception) {}
            bookDetailState = bookDetailState.copy(isLoading = false)
        }
    }

    fun searchBooks(query: String) {
        searchState = searchState.copy(query = query)
        if (query.isBlank()) {
            searchState = searchState.copy(results = emptyList())
            return
        }
        viewModelScope.launch {
            try {
                val results = bookRepository.searchBooks(query).first()
                searchState = searchState.copy(results = results)
            } catch (_: Exception) {}
        }
    }

    fun loadBorrowedBooks() {
        viewModelScope.launch {
            borrowedState = borrowedState.copy(isLoading = true)
            try {
                val transactions = transactionRepository.getBorrowedBooksByStudent(currentStudentId).first()
                borrowedState = borrowedState.copy(borrowedBooks = transactions, isLoading = false)
            } catch (_: Exception) {
                borrowedState = borrowedState.copy(isLoading = false)
            }
        }
    }

    fun loadLeaderboard() {
        viewModelScope.launch {
            try {
                val list = studentRepository.getLeaderboard().first()
                homeState = homeState.copy(leaderboard = list.take(10))
            } catch (_: Exception) {}
        }
    }

    fun borrowBook(bookId: Int) {
        viewModelScope.launch {
            try {
                val book = bookRepository.getBookById(bookId) ?: return@launch
                if (book.availableCopies <= 0) { snackbarMessage = "No copies available"; return@launch }
                val dueDate = System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000L
                val student = studentRepository.getStudentById(currentStudentId)

                transactionRepository.insertTransaction(
                    TransactionEntity(
                        bookId = bookId,
                        studentId = currentStudentId,
                        studentName = student?.name ?: "",
                        bookTitle = book.title,
                        dueDate = dueDate
                    )
                )

                bookRepository.updateBook(book.copy(availableCopies = book.availableCopies - 1))
                snackbarMessage = "Book borrowed! Due in 14 days"
                loadBookDetail(bookId)
                loadHomeData()
                loadBorrowedBooks()
            } catch (_: Exception) { snackbarMessage = "Failed to borrow book" }
        }
    }

    fun returnBook(bookId: Int) {
        viewModelScope.launch {
            try {
                val transactions = transactionRepository.getBorrowedBooksByStudent(currentStudentId).first()
                val active = transactions.find { it.bookId == bookId && !it.isReturned }
                if (active != null) {
                    transactionRepository.updateTransaction(
                        active.copy(isReturned = true, returnedDate = System.currentTimeMillis())
                    )
                    val student = studentRepository.getStudentById(currentStudentId)
                    if (student != null) {
                        studentRepository.updateStudent(
                            student.copy(totalBooksRead = student.totalBooksRead + 1, points = student.points + 10)
                        )
                    }
                } else { snackbarMessage = "No active borrow found"; return@launch }
                val book = bookRepository.getBookById(bookId)
                if (book != null) {
                    bookRepository.updateBook(book.copy(availableCopies = book.availableCopies + 1))
                }
                snackbarMessage = "Book returned! +10 points"
                loadBookDetail(bookId)
                loadHomeData()
                loadBorrowedBooks()
            } catch (_: Exception) { snackbarMessage = "Failed to return book" }
        }
    }

    fun loadProfile() {
        viewModelScope.launch {
            profileState = profileState.copy(isLoading = true)
            try {
                val student = studentRepository.getStudentById(currentStudentId)
                profileState = profileState.copy(student = student, isLoading = false)
            } catch (_: Exception) {
                profileState = profileState.copy(isLoading = false)
            }
        }
    }
}
