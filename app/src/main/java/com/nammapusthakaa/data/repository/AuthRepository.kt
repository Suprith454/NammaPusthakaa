package com.nammapusthakaa.data.repository

import com.nammapusthakaa.data.local.dao.UserDao
import com.nammapusthakaa.data.local.entity.UserEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun login(email: String, password: String): UserEntity? {
        return userDao.login(email, password)
    }

    suspend fun register(user: UserEntity): Long {
        return userDao.insertUser(user)
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    suspend fun getUserById(id: Int): UserEntity? {
        return userDao.getUserById(id)
    }

    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }
}
