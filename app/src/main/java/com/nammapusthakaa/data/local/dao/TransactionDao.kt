package com.nammapusthakaa.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nammapusthakaa.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY issueDate DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE studentId = :studentId ORDER BY issueDate DESC")
    fun getTransactionsByStudent(studentId: Int): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE bookId = :bookId ORDER BY issueDate DESC")
    fun getTransactionsByBook(bookId: Int): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE isReturned = 0")
    fun getActiveIssues(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE isReturned = 0 AND dueDate < :currentTime")
    fun getOverdueTransactions(currentTime: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE isReturned = 0 AND studentId = :studentId")
    fun getBorrowedBooksByStudent(studentId: Int): Flow<List<TransactionEntity>>

    @Query("SELECT COUNT(*) FROM transactions WHERE isReturned = 0")
    fun getActiveIssuesCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM transactions")
    fun getTotalTransactionsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM transactions WHERE isReturned = 0 AND dueDate < :currentTime")
    fun getOverdueCount(currentTime: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM transactions WHERE issueDate >= :startOfMonth AND issueDate < :endOfMonth")
    suspend fun getMonthlyIssueCount(startOfMonth: Long, endOfMonth: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE bookId = :bookId")
    suspend fun deleteTransactionsByBook(bookId: Int)

    @Query("DELETE FROM transactions WHERE studentId = :studentId")
    suspend fun deleteTransactionsByStudent(studentId: Int)

    @Query("DELETE FROM transactions WHERE id IN (SELECT id FROM transactions WHERE bookId IN (SELECT id FROM books WHERE addedByUserId = :userId))")
    suspend fun deleteTransactionsByTeacher(userId: Int)
}
