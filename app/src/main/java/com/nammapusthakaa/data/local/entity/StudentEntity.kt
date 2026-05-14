package com.nammapusthakaa.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int = 0,
    val name: String,
    val registerNumber: String = "",
    val className: String,
    val totalBooksRead: Int = 0,
    val totalPagesRead: Int = 0,
    val totalReviewsSubmitted: Int = 0,
    val points: Int = 0
)
