package com.nammapusthakaa.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val bookId: Int,
    val studentId: Int,
    val studentName: String = "",
    val rating: Float,
    val review: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
