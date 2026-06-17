package com.example.gamingspace.data.repository

import com.example.gamingspace.data.dao.MemberDao
import com.example.gamingspace.data.dao.TransactionDao
import com.example.gamingspace.data.model.Member
import com.example.gamingspace.data.model.Transaction
import kotlinx.coroutines.flow.Flow

class GamingRepository(
    private val memberDao: MemberDao,
    private val transactionDao: TransactionDao
) {
    fun getAllMembers(): Flow<List<Member>> = memberDao.getAllMembers()

    suspend fun insertMember(member: Member) = memberDao.insertMember(member)

    suspend fun updateMember(member: Member) = memberDao.updateMember(member)

    suspend fun deleteMember(member: Member) = memberDao.deleteMember(member) // ← TAMBAH INI

    suspend fun getMemberById(id: Int): Member? = memberDao.getMemberById(id)

    suspend fun insertTransaction(transaction: Transaction) =
        transactionDao.insertTransaction(transaction)

    fun getTransactionsByMember(memberId: Int): Flow<List<Transaction>> =
        transactionDao.getTransactionsByMember(memberId)
}