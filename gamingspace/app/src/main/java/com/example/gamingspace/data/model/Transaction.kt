package com.example.gamingspace.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val memberId: Int,
    val amount: Double,
    val pointEarned: Int,
    val date: String
)