package com.example.practise.presentation.screens.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practise.data.local.dao.TransactionDao
import com.example.practise.data.local.entity.TransactionEntity
import com.example.practise.domain.model.CurrencyConversion
import com.example.practise.domain.repository.CurrencyConversionRepository
import com.example.practise.domain.repository.RepositoryResult
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currencyConversionRepository: CurrencyConversionRepository,
    private val transactionDao: TransactionDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadSupportedCurrencies()
        observeTransactions()
    }

    private fun observeTransactions() {
        viewModelScope.launch {
            transactionDao.getAllTransactions().collect { list ->
                _uiState.update { it.copy(transactions = list) }
            }
        }
    }

    fun loadSupportedCurrencies() {
        if (_uiState.value.isLoadingCurrencies) return
        _uiState.update {
            it.copy(isLoadingCurrencies = true, errorMessage = null)
        }

        viewModelScope.launch {
            when (val result = currencyConversionRepository.getSupportedCurrencies(INR)) {
                is RepositoryResult.Success -> _uiState.update {
                    it.copy(
                        supportedCurrencies = result.data,
                        isLoadingCurrencies = false
                    )
                }
                is RepositoryResult.Failure -> _uiState.update {
                    it.copy(
                        isLoadingCurrencies = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun deposit(amountText: String, currencyCode: String) {
        val amount = amountText.toValidAmount()
        if (amount == null) {
            showError("Enter a valid amount greater than zero (up to 2 decimal places).")
            return
        }
        if (currencyCode !in _uiState.value.supportedCurrencies) {
            showError("Select a supported currency.")
            return
        }
        if (_uiState.value.isProcessingOperation) return

        _uiState.update {
            it.copy(isProcessingOperation = true, errorMessage = null)
        }
        viewModelScope.launch {
            val conversionResult = if (currencyCode == INR) {
                RepositoryResult.Success(
                    CurrencyConversion(
                        baseCurrency = INR,
                        targetCurrency = INR,
                        conversionRate = BigDecimal.ONE,
                        convertedAmount = amount
                    )
                )
            } else {
                currencyConversionRepository.convertCurrency(
                    baseCurrency = currencyCode,
                    targetCurrency = INR,
                    amount = amount
                )
            }

            when (conversionResult) {
                is RepositoryResult.Success -> completeDeposit(
                    conversion = conversionResult.data
                )
                is RepositoryResult.Failure -> _uiState.update {
                    it.copy(
                        isProcessingOperation = false,
                        errorMessage = conversionResult.message
                    )
                }
            }
        }
    }

    fun withdraw(amountText: String) {
        val amount = amountText.toValidAmount()
        if (amount == null) {
            showError("Enter a valid INR amount greater than zero (up to 2 decimal places).")
            return
        }
        if (_uiState.value.isProcessingOperation) return
        if (amount > _uiState.value.balanceInInr) {
            showError("Insufficient funds. Your available balance is ₹ ${_uiState.value.balanceInInr.toDisplayAmount()}.")
            return
        }

        _uiState.update {
            it.copy(
                balanceInInr = it.balanceInInr.subtract(amount).moneyScale(),
                errorMessage = null,
                operationCompleted = true
            )
        }

        viewModelScope.launch {
            transactionDao.insertTransaction(
                TransactionEntity(
                    type = "WITHDRAWAL",
                    title = "Withdrawal",
                    amount = amount.toPlainString(),
                    currency = "INR",
                    inrAmount = amount.toPlainString()
                )
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun clearOperationCompleted() {
        _uiState.update { it.copy(operationCompleted = false) }
    }

    private fun completeDeposit(
        conversion: CurrencyConversion
    ) {
        if (conversion.targetCurrency != INR || conversion.convertedAmount <= BigDecimal.ZERO) {
            _uiState.update {
                it.copy(
                    isProcessingOperation = false,
                    errorMessage = "The conversion service returned an invalid INR amount."
                )
            }
            return
        }

        val convertedAmount = conversion.convertedAmount.moneyScale()
        _uiState.update {
            it.copy(
                balanceInInr = it.balanceInInr.add(convertedAmount).moneyScale(),
                isProcessingOperation = false,
                errorMessage = null,
                operationCompleted = true
            )
        }

        viewModelScope.launch {
            transactionDao.insertTransaction(
                TransactionEntity(
                    type = "DEPOSIT",
                    title = "Deposit (${conversion.baseCurrency})",
                    amount = conversion.convertedAmount.toPlainString(),
                    currency = conversion.baseCurrency,
                    inrAmount = convertedAmount.toPlainString()
                )
            )
        }
    }

    private fun showError(message: String) {
        _uiState.update { it.copy(errorMessage = message) }
    }

    private fun String.toValidAmount(): BigDecimal? {
        val amount = toBigDecimalOrNull() ?: return null
        if (amount <= BigDecimal.ZERO || amount.stripTrailingZeros().scale() > 2) return null
        return amount
    }

    private fun BigDecimal.moneyScale(): BigDecimal =
        setScale(2, RoundingMode.HALF_UP)

    private fun BigDecimal.toDisplayAmount(): String =
        setScale(2, RoundingMode.HALF_UP).toPlainString()

    private companion object {
        const val INR = "INR"
    }
}
