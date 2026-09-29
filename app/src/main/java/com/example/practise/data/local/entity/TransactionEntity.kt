package com.example.practise.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // "DEPOSIT" or "WITHDRAWAL"
    val title: String,
    val amount: String,
    val currency: String,
    val inrAmount: String,
    val timestamp: Long = System.currentTimeMillis()
)
