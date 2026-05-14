package com.nammapusthakaa.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String,
    val category: String,
    val language: String,
    val copies: Int,
    val availableCopies: Int = 0,
    val qrCode: String,
    val description: String = "",
    val kannadaSummary: String = "",
    val coverUrl: String = "",
    val addedByUserId: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
