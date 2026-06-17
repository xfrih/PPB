package com.example.gamingspace.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gamingspace.data.dao.MemberDao
import com.example.gamingspace.data.dao.TransactionDao
import com.example.gamingspace.data.model.Member
import com.example.gamingspace.data.model.Transaction

@Database(
    entities = [Member::class, Transaction::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun memberDao(): MemberDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gaming_space_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}