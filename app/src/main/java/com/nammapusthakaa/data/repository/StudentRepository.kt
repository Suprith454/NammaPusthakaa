package com.nammapusthakaa.data.repository

import com.nammapusthakaa.data.local.dao.StudentDao
import com.nammapusthakaa.data.local.entity.StudentEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentRepository @Inject constructor(
    private val studentDao: StudentDao
) {
    fun getAllStudents(): Flow<List<StudentEntity>> = studentDao.getAllStudents()

    suspend fun getStudentById(id: Int): StudentEntity? = studentDao.getStudentById(id)

    suspend fun getStudentByUserId(userId: Int): StudentEntity? = studentDao.getStudentByUserId(userId)

    suspend fun getStudentByRegisterNumber(registerNumber: String): StudentEntity? = studentDao.getStudentByRegisterNumber(registerNumber)

    fun getLeaderboard(): Flow<List<StudentEntity>> = studentDao.getLeaderboard()

    fun getLeaderboardByBooksRead(): Flow<List<StudentEntity>> = studentDao.getLeaderboardByBooksRead()

    suspend fun insertStudent(student: StudentEntity): Long = studentDao.insertStudent(student)

    suspend fun updateStudent(student: StudentEntity) = studentDao.updateStudent(student)

    fun getStudentCount(): Flow<Int> = studentDao.getStudentCount()

    suspend fun deleteStudent(student: StudentEntity) = studentDao.deleteStudent(student)
}
