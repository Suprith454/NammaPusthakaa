package com.nammapusthakaa.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nammapusthakaa.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY createdAt DESC")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBookById(id: Int): BookEntity?

    @Query("SELECT * FROM books WHERE id = :id")
    fun getBookByIdFlow(id: Int): Flow<BookEntity?>

    @Query("SELECT * FROM books WHERE availableCopies > 0")
    fun getAvailableBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE category = :category")
    fun getBooksByCategory(category: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%'")
    fun searchBooks(query: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM books ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentBooks(limit: Int): Flow<List<BookEntity>>

    @Query("SELECT DISTINCT category FROM books")
    fun getAllCategories(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Update
    suspend fun updateBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(book: BookEntity)

    @Query("DELETE FROM books WHERE id = :id")
    suspend fun deleteBookById(id: Int)

    @Query("SELECT COUNT(*) FROM books")
    fun getBookCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM books WHERE availableCopies > 0")
    fun getAvailableBookCount(): Flow<Int>
}
