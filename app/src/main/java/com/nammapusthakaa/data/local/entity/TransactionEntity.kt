package com.nammapusthakaa.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val bookId: Int,
    val studentId: Int,
    val studentName: String = "",
    val bookTitle: String = "",
    val issueDate: Long = System.currentTimeMillis(),
    val dueDate: Long,
    val returnedDate: Long? = null,
    val isReturned: Boolean = false,
    val isOverdue: Boolean = false
)
