package com.example.practise.data.remote

import com.example.practise.data.remote.model.ConversionResponse
import com.example.practise.data.remote.model.LatestRatesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {
    @GET("pair/{base}/{target}/{amount}")
    suspend fun convertCurrency(
        @Path("base") baseCurrency: String,
        @Path("target") targetCurrency: String,
        @Path("amount") amount: String
    ): ConversionResponse

    @GET("latest/{base}")
    suspend fun getLatestRates(
        @Path("base") baseCurrency: String
    ): LatestRatesResponse
}
