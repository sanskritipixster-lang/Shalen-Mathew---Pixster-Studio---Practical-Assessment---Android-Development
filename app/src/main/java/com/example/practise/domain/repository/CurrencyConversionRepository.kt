package com.example.practise.domain.repository

import com.example.practise.domain.model.CurrencyConversion
import java.math.BigDecimal

interface CurrencyConversionRepository {
    suspend fun convertCurrency(
        baseCurrency: String,
        targetCurrency: String,
        amount: BigDecimal
    ): RepositoryResult<CurrencyConversion>

    suspend fun getSupportedCurrencies(baseCurrency: String): RepositoryResult<List<String>>
}

sealed interface RepositoryResult<out T> {
    data class Success<T>(val data: T) : RepositoryResult<T>

    data class Failure(
        val type: FailureType,
        val message: String
    ) : RepositoryResult<Nothing>
}

enum class FailureType {
    API,
    NETWORK,
    HTTP,
    INVALID_RESPONSE
}
