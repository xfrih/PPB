package com.example.gamingspace.data.dao

import androidx.room.*
import com.example.gamingspace.data.model.Member
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {

    @Insert
    suspend fun insertMember(member: Member)

    @Update
    suspend fun updateMember(member: Member)

    @Delete
    suspend fun deleteMember(member: Member)

    @Query("SELECT * FROM members ORDER BY id DESC")
    fun getAllMembers(): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE id = :id")
    suspend fun getMemberById(id: Int): Member?
}