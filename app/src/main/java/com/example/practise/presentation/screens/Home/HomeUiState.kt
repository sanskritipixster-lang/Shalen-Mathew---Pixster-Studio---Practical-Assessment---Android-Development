package com.example.practise.presentation.screens.Home

import com.example.practise.data.local.entity.TransactionEntity
import java.math.BigDecimal

data class HomeUiState(
    val balanceInInr: BigDecimal = BigDecimal("24224.90"),
    val supportedCurrencies: List<String> = emptyList(),
    val transactions: List<TransactionEntity> = emptyList(),
    val isLoadingCurrencies: Boolean = false,
    val isProcessingOperation: Boolean = false,
    val errorMessage: String? = null,
    val operationCompleted: Boolean = false
)
