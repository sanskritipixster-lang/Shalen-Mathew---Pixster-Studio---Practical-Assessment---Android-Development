package com.example.practise.data.remote.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class LatestRatesResponse(
    val result: String? = null,
    @SerializedName("conversion_rates")
    val conversionRates: Map<String, BigDecimal>? = null,
    @SerializedName("error-type")
    val errorType: String? = null
)
