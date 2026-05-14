package com.nammapusthakaa.data.repository

import com.nammapusthakaa.data.local.dao.TransactionDao
import com.nammapusthakaa.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) {
    fun getAllTransactions(): Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()

    suspend fun getTransactionById(id: Int): TransactionEntity? = transactionDao.getTransactionById(id)

    fun getTransactionsByStudent(studentId: Int): Flow<List<TransactionEntity>> = transactionDao.getTransactionsByStudent(studentId)

    fun getTransactionsByBook(bookId: Int): Flow<List<TransactionEntity>> = transactionDao.getTransactionsByBook(bookId)

    fun getActiveIssues(): Flow<List<TransactionEntity>> = transactionDao.getActiveIssues()

    fun getOverdueTransactions(currentTime: Long): Flow<List<TransactionEntity>> = transactionDao.getOverdueTransactions(currentTime)

    fun getBorrowedBooksByStudent(studentId: Int): Flow<List<TransactionEntity>> = transactionDao.getBorrowedBooksByStudent(studentId)

    fun getActiveIssuesCount(): Flow<Int> = transactionDao.getActiveIssuesCount()

    fun getTotalTransactionsCount(): Flow<Int> = transactionDao.getTotalTransactionsCount()

    fun getOverdueCount(currentTime: Long): Flow<Int> = transactionDao.getOverdueCount(currentTime)

    suspend fun getMonthlyIssueCount(startOfMonth: Long, endOfMonth: Long): Int = transactionDao.getMonthlyIssueCount(startOfMonth, endOfMonth)

    suspend fun insertTransaction(transaction: TransactionEntity): Long = transactionDao.insertTransaction(transaction)

    suspend fun updateTransaction(transaction: TransactionEntity) = transactionDao.updateTransaction(transaction)

    suspend fun deleteTransaction(transaction: TransactionEntity) = transactionDao.deleteTransaction(transaction)

    suspend fun deleteTransactionsByBook(bookId: Int) = transactionDao.deleteTransactionsByBook(bookId)

    suspend fun deleteTransactionsByStudent(studentId: Int) = transactionDao.deleteTransactionsByStudent(studentId)
}
