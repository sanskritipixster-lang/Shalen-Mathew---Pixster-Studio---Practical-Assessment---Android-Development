package com.example.practise.domain.model

import java.math.BigDecimal

data class CurrencyConversion(
    val baseCurrency: String,
    val targetCurrency: String,
    val conversionRate: BigDecimal,
    val convertedAmount: BigDecimal
)
