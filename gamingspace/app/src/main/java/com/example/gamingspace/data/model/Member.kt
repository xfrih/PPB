package com.example.gamingspace.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class Member(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String,
    val points: Int = 0,
    val joinDate: String = "",
    val remainingHours: Double = 0.0
)