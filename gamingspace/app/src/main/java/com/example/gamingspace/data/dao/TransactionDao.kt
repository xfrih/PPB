package com.example.gamingspace.data.dao

import androidx.room.*
import com.example.gamingspace.data.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE memberId = :memberId ORDER BY id DESC")
    fun getTransactionsByMember(memberId: Int): Flow<List<Transaction>>
}