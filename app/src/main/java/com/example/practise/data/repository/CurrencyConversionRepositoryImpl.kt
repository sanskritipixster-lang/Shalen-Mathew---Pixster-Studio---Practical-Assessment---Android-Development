package com.example.practise.data.repository

import com.example.practise.data.remote.ExchangeRateApi
import com.example.practise.domain.model.CurrencyConversion
import com.example.practise.domain.repository.CurrencyConversionRepository
import com.example.practise.domain.repository.FailureType
import com.example.practise.domain.repository.RepositoryResult
import retrofit2.HttpException
import java.io.IOException
import java.math.BigDecimal
import javax.inject.Inject

class CurrencyConversionRepositoryImpl @Inject constructor(
    private val exchangeRateApi: ExchangeRateApi
) : CurrencyConversionRepository {
    override suspend fun convertCurrency(
        baseCurrency: String,
        targetCurrency: String,
        amount: BigDecimal
    ): RepositoryResult<CurrencyConversion> {
        return try {
            val response = exchangeRateApi.convertCurrency(
                baseCurrency = baseCurrency,
                targetCurrency = targetCurrency,
                amount = amount.toPlainString()
            )

            if (response.result != "success") {
                return RepositoryResult.Failure(
                    type = FailureType.API,
                    message = response.errorType ?: "Currency conversion failed."
                )
            }

            val conversionRate = response.conversionRate
            val convertedAmount = response.conversionResult
            val responseBaseCurrency = response.baseCode
            val responseTargetCurrency = response.targetCode
            if (
                conversionRate == null ||
                convertedAmount == null ||
                responseBaseCurrency == null ||
                responseTargetCurrency == null
            ) {
                return RepositoryResult.Failure(
                    type = FailureType.INVALID_RESPONSE,
                    message = "The conversion response was incomplete."
                )
            }

            RepositoryResult.Success(
                CurrencyConversion(
                    baseCurrency = responseBaseCurrency,
                    targetCurrency = responseTargetCurrency,
                    conversionRate = conversionRate,
                    convertedAmount = convertedAmount
                )
            )
        } catch (exception: HttpException) {
            RepositoryResult.Failure(
                type = FailureType.HTTP,
                message = "The conversion service returned HTTP ${exception.code()}."
            )
        } catch (exception: IOException) {
            RepositoryResult.Failure(
                type = FailureType.NETWORK,
                message = "Unable to reach the conversion service. Check your connection."
            )
        }
    }

    override suspend fun getSupportedCurrencies(
        baseCurrency: String
    ): RepositoryResult<List<String>> {
        return try {
            val response = exchangeRateApi.getLatestRates(baseCurrency)
            if (response.result != "success") {
                return RepositoryResult.Failure(
                    type = FailureType.API,
                    message = response.errorType ?: "Unable to load supported currencies."
                )
            }

            val rates = response.conversionRates
            if (rates.isNullOrEmpty()) {
                return RepositoryResult.Failure(
                    type = FailureType.INVALID_RESPONSE,
                    message = "The currency list response was empty."
                )
            }

            RepositoryResult.Success(rates.keys.sorted())
        } catch (exception: HttpException) {
            RepositoryResult.Failure(
                type = FailureType.HTTP,
                message = "The conversion service returned HTTP ${exception.code()}."
            )
        } catch (exception: IOException) {
            RepositoryResult.Failure(
                type = FailureType.NETWORK,
                message = "Unable to reach the conversion service. Check your connection."
            )
        }
    }
}
