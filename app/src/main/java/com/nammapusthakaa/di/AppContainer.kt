package com.nammapusthakaa.di

import android.content.Context
import com.nammapusthakaa.data.local.AppDatabase
import com.nammapusthakaa.data.repository.AuthRepository
import com.nammapusthakaa.data.repository.BookRepository
import com.nammapusthakaa.data.repository.ReviewRepository
import com.nammapusthakaa.data.repository.StudentRepository
import com.nammapusthakaa.data.repository.TransactionRepository
import com.nammapusthakaa.data.repository.UserRepository

class AppContainer(context: Context) {
    private val database = AppDatabase.getDatabase(context)

    private val bookDao = database.bookDao()
    private val userDao = database.userDao()
    private val studentDao = database.studentDao()
    private val transactionDao = database.transactionDao()
    private val reviewDao = database.reviewDao()

    val bookRepository = BookRepository(bookDao)
    val authRepository = AuthRepository(userDao)
    val studentRepository = StudentRepository(studentDao)
    val transactionRepository = TransactionRepository(transactionDao)
    val reviewRepository = ReviewRepository(reviewDao)
    val userRepository = UserRepository(userDao)
}
