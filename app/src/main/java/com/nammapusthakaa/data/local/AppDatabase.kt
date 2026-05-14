package com.nammapusthakaa.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nammapusthakaa.data.local.dao.BookDao
import com.nammapusthakaa.data.local.dao.ReviewDao
import com.nammapusthakaa.data.local.dao.StudentDao
import com.nammapusthakaa.data.local.dao.TransactionDao
import com.nammapusthakaa.data.local.dao.UserDao
import com.nammapusthakaa.data.local.entity.BookEntity
import com.nammapusthakaa.data.local.entity.ReviewEntity
import com.nammapusthakaa.data.local.entity.StudentEntity
import com.nammapusthakaa.data.local.entity.TransactionEntity
import com.nammapusthakaa.data.local.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [BookEntity::class, UserEntity::class, StudentEntity::class, TransactionEntity::class, ReviewEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun userDao(): UserDao
    abstract fun studentDao(): StudentDao
    abstract fun transactionDao(): TransactionDao
    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "namma_pusthakaa_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = database
                CoroutineScope(Dispatchers.IO).launch {
                    val existing = database.userDao().getUserByEmail("admin@nammapustaka.com")
                    if (existing == null) {
                        database.userDao().insertUser(
                            UserEntity(
                                name = "Admin",
                                email = "admin@nammapustaka.com",
                                password = "admin123",
                                role = "Admin"
                            )
                        )
                    }
                }
                database
            }
        }
    }
}
