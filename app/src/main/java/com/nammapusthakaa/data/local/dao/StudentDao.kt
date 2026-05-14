package com.nammapusthakaa.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nammapusthakaa.data.local.entity.StudentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Query("SELECT * FROM students")
    fun getAllStudents(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE id = :id")
    suspend fun getStudentById(id: Int): StudentEntity?

    @Query("SELECT * FROM students WHERE userId = :userId")
    suspend fun getStudentByUserId(userId: Int): StudentEntity?

    @Query("SELECT * FROM students WHERE registerNumber = :registerNumber LIMIT 1")
    suspend fun getStudentByRegisterNumber(registerNumber: String): StudentEntity?

    @Query("SELECT * FROM students ORDER BY points DESC")
    fun getLeaderboard(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students ORDER BY totalBooksRead DESC")
    fun getLeaderboardByBooksRead(): Flow<List<StudentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity): Long

    @Update
    suspend fun updateStudent(student: StudentEntity)

    @Delete
    suspend fun deleteStudent(student: StudentEntity)

    @Query("SELECT COUNT(*) FROM students")
    fun getStudentCount(): Flow<Int>
}
