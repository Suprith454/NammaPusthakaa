package com.nammapusthakaa.data.repository

import com.nammapusthakaa.data.local.dao.UserDao
import com.nammapusthakaa.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()

    suspend fun getUserById(id: Int): UserEntity? = userDao.getUserById(id)

    fun getUsersByRole(role: String): Flow<List<UserEntity>> = userDao.getUsersByRole(role)

    fun getUserCount(): Flow<Int> = userDao.getUserCount()

    suspend fun deleteUser(user: UserEntity) = userDao.deleteUser(user)

    suspend fun deleteUserById(id: Int) = userDao.deleteUserById(id)

    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)
}
