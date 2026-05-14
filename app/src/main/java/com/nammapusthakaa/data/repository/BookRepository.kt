package com.nammapusthakaa.data.repository

import com.nammapusthakaa.data.local.dao.BookDao
import com.nammapusthakaa.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val bookDao: BookDao
) {
    fun getAllBooks(): Flow<List<BookEntity>> = bookDao.getAllBooks()

    suspend fun getBookById(id: Int): BookEntity? = bookDao.getBookById(id)

    fun getBookByIdFlow(id: Int): Flow<BookEntity?> = bookDao.getBookByIdFlow(id)

    fun getAvailableBooks(): Flow<List<BookEntity>> = bookDao.getAvailableBooks()

    fun getBooksByCategory(category: String): Flow<List<BookEntity>> = bookDao.getBooksByCategory(category)

    fun searchBooks(query: String): Flow<List<BookEntity>> = bookDao.searchBooks(query)

    fun getRecentBooks(limit: Int): Flow<List<BookEntity>> = bookDao.getRecentBooks(limit)

    fun getAllCategories(): Flow<List<String>> = bookDao.getAllCategories()

    suspend fun insertBook(book: BookEntity): Long = bookDao.insertBook(book)

    suspend fun updateBook(book: BookEntity) = bookDao.updateBook(book)

    suspend fun deleteBook(book: BookEntity) = bookDao.deleteBook(book)

    suspend fun deleteBookById(id: Int) = bookDao.deleteBookById(id)

    fun getBookCount(): Flow<Int> = bookDao.getBookCount()

    fun getAvailableBookCount(): Flow<Int> = bookDao.getAvailableBookCount()
}
