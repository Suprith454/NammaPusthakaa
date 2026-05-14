package com.nammapusthakaa.data.repository

import com.nammapusthakaa.data.local.dao.BookRatingResult
import com.nammapusthakaa.data.local.dao.ReviewDao
import com.nammapusthakaa.data.local.entity.ReviewEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepository @Inject constructor(
    private val reviewDao: ReviewDao
) {
    fun getAllReviews(): Flow<List<ReviewEntity>> = reviewDao.getAllReviews()

    fun getReviewsByBook(bookId: Int): Flow<List<ReviewEntity>> = reviewDao.getReviewsByBook(bookId)

    fun getReviewsByStudent(studentId: Int): Flow<List<ReviewEntity>> = reviewDao.getReviewsByStudent(studentId)

    fun getAverageRatingForBook(bookId: Int): Flow<Float?> = reviewDao.getAverageRatingForBook(bookId)

    fun getReviewCountForBook(bookId: Int): Flow<Int> = reviewDao.getReviewCountForBook(bookId)

    fun getTopRatedBooks(): Flow<List<BookRatingResult>> = reviewDao.getTopRatedBooks()

    fun getTotalReviewsCount(): Flow<Int> = reviewDao.getTotalReviewsCount()

    suspend fun insertReview(review: ReviewEntity): Long = reviewDao.insertReview(review)

    suspend fun updateReview(review: ReviewEntity) = reviewDao.updateReview(review)

    suspend fun deleteReview(review: ReviewEntity) = reviewDao.deleteReview(review)
}
