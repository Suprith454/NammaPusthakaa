package com.nammapusthakaa.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nammapusthakaa.data.local.entity.ReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews ORDER BY createdAt DESC")
    fun getAllReviews(): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews WHERE bookId = :bookId ORDER BY createdAt DESC")
    fun getReviewsByBook(bookId: Int): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews WHERE studentId = :studentId ORDER BY createdAt DESC")
    fun getReviewsByStudent(studentId: Int): Flow<List<ReviewEntity>>

    @Query("SELECT AVG(rating) FROM reviews WHERE bookId = :bookId")
    fun getAverageRatingForBook(bookId: Int): Flow<Float?>

    @Query("SELECT COUNT(*) FROM reviews WHERE bookId = :bookId")
    fun getReviewCountForBook(bookId: Int): Flow<Int>

    @Query("SELECT bookId, AVG(rating) as avgRating, COUNT(*) as reviewCount FROM reviews GROUP BY bookId ORDER BY avgRating DESC")
    fun getTopRatedBooks(): Flow<List<BookRatingResult>>

    @Query("SELECT COUNT(*) FROM reviews")
    fun getTotalReviewsCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity): Long

    @Update
    suspend fun updateReview(review: ReviewEntity)

    @Delete
    suspend fun deleteReview(review: ReviewEntity)
}

data class BookRatingResult(
    val bookId: Int,
    val avgRating: Double,
    val reviewCount: Int
)
