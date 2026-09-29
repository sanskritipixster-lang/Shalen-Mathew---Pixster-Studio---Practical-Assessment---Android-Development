package com.example.practise.presentation.screens.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.practise.data.local.entity.TransactionEntity
import com.example.practise.presentation.screens.Home.util.HomeHeader
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val HomeBlue = Color(0xFF087FF5)
private val HistoryBackground = Color(0xFFF4F6F8)
private val PrimaryText = Color(0xFF263B50)
private val MutedText = Color(0xFF929BA4)
private val KeypadBackground = Color(0xFF092A45)
private val KeyBackground = Color(0xFF29435A)
private val ConfirmGreen = Color(0xFF36D66B)
private val DeleteRed = Color(0xFFFF5947)

private enum class OperationType {
    DEPOSIT,
    WITHDRAWAL
}

@Composable
fun HomeScreen(
    navHost: NavHostController,
    paddingValues: PaddingValues,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var activeOperation by remember { mutableStateOf<OperationType?>(null) }
    var amount by remember { mutableStateOf("0") }
    var selectedCurrency by remember { mutableStateOf("") }

    LaunchedEffect(uiState.operationCompleted) {
        if (uiState.operationCompleted) {
            activeOperation = null
            amount = "0"
            viewModel.clearOperationCompleted()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HomeBlue)
    ) {
        HomeHeader(
            balance = "₹ ${uiState.balanceInInr.formatAmount(2)}",
            onWithdrawClick = {
                amount = "0"
                viewModel.clearError()
                activeOperation = OperationType.WITHDRAWAL
            },
            onDepositClick = {
                amount = "0"
                selectedCurrency = if ("USD" in uiState.supportedCurrencies) {
                    "USD"
                } else {
                    uiState.supportedCurrencies.firstOrNull().orEmpty()
                }
                viewModel.clearError()
                activeOperation = OperationType.DEPOSIT
            }
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(HistoryBackground),
            contentPadding = PaddingValues(start = 14.dp, top = 9.dp, end = 14.dp, bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Column {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 9.dp)
                            .size(width = 30.dp, height = 4.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE2E5E8))
                    )
                    Text(
                        text = "History",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp),
                        color = PrimaryText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            if (uiState.transactions.isEmpty()) {
                item {
                    Text(
                        text = "Transaction history will appear here.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        color = MutedText,
                        fontSize = 13.sp
                    )
                }
            } else {
                items(uiState.transactions) { tx ->
                    TransactionItem(tx)
                }
            }
        }
    }

    activeOperation?.let { operation ->
        Dialog(
            onDismissRequest = {
                if (!uiState.isProcessingOperation) {
                    activeOperation = null
                    viewModel.clearError()
                }
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            AmountKeypad(
                operation = operation,
                amount = amount,
                currency = selectedCurrency,
                supportedCurrencies = uiState.supportedCurrencies,
                isLoadingCurrencies = uiState.isLoadingCurrencies,
                isProcessing = uiState.isProcessingOperation,
                errorMessage = uiState.errorMessage,
                onCurrencySelected = {
                    selectedCurrency = it
                    viewModel.clearError()
                },
                onAmountChanged = {
                    amount = it
                    viewModel.clearError()
                },
                onRetryCurrencies = viewModel::loadSupportedCurrencies,
                onConfirm = {
                    if (operation == OperationType.DEPOSIT) {
                        viewModel.deposit(amount, selectedCurrency)
                    } else {
                        viewModel.withdraw(amount)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 360.dp)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun TransactionItem(tx: TransactionEntity) {
    val isDeposit = tx.type == "DEPOSIT"
    val amountColor = if (isDeposit) Color(0xFF10B981) else Color(0xFFEF4444)
    val sign = if (isDeposit) "+" else "-"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = tx.title,
                color = PrimaryText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(tx.timestamp)),
                color = MutedText,
                fontSize = 11.sp
            )
        }
        Text(
            text = "$sign ₹ ${BigDecimal(tx.inrAmount).formatAmount(2)}",
            color = amountColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AmountKeypad(
    operation: OperationType,
    amount: String,
    currency: String,
    supportedCurrencies: List<String>,
    isLoadingCurrencies: Boolean,
    isProcessing: Boolean,
    errorMessage: String?,
    onCurrencySelected: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onRetryCurrencies: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currencyMenuExpanded by remember { mutableStateOf(false) }
    val deposit = operation == OperationType.DEPOSIT

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(KeypadBackground)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(0.45f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp))
                    .background(HomeBlue)
                    .clickable(enabled = deposit && !isLoadingCurrencies) {
                        currencyMenuExpanded = true
                    }
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                if (deposit && isLoadingCurrencies && supportedCurrencies.isEmpty()) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(19.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if (deposit) {
                            "${currency.currencySymbol()}  ▾"
                        } else {
                            "₹  INR"
                        },
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                DropdownMenu(
                    expanded = currencyMenuExpanded,
                    onDismissRequest = { currencyMenuExpanded = false },
                    modifier = Modifier.heightIn(max = 320.dp)
                ) {
                    supportedCurrencies.forEach { code ->
                        DropdownMenuItem(
                            text = { Text("${code.currencySymbol()}  $code") },
                            onClick = {
                                onCurrencySelected(code)
                                currencyMenuExpanded = false
                            }
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = amount,
                    color = PrimaryText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier.weight(3f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                KeypadKeyRow(amount, listOf("7", "8", "9"), onAmountChanged, isProcessing)
                KeypadKeyRow(amount, listOf("4", "5", "6"), onAmountChanged, isProcessing)
            }
            KeypadKey(
                label = "AC",
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp),
                enabled = !isProcessing,
                onClick = { onAmountChanged("0") }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf("1", "2", "3").forEach { digit ->
                KeypadKey(
                    label = digit,
                    modifier = Modifier.weight(1f),
                    enabled = !isProcessing,
                    onClick = { onAmountChanged(amount.appendKey(digit)) }
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DeleteRed)
                    .clickable(enabled = !isProcessing) {
                        onAmountChanged(amount.dropLast(1).ifEmpty { "0" })
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Delete last digit",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf("0", "00", ".").forEach { value ->
                KeypadKey(
                    label = value,
                    modifier = Modifier.weight(1f),
                    enabled = !isProcessing,
                    onClick = { onAmountChanged(amount.appendKey(value)) }
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .clip(CircleShape)
                    .background(if (isProcessing) ConfirmGreen.copy(alpha = 0.5f) else ConfirmGreen)
                    .clickable(enabled = !isProcessing, onClick = onConfirm),
                contentAlignment = Alignment.Center
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Confirm ${operation.name.lowercase()}",
                        modifier = Modifier.size(22.dp),
                        tint = Color.White
                    )
                }
            }
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color(0xFFFFC6BE),
                fontSize = 12.sp
            )
            if (deposit && supportedCurrencies.isEmpty() && !isLoadingCurrencies) {
                Text(
                    text = "Retry loading currencies",
                    modifier = Modifier
                        .clickable(onClick = onRetryCurrencies)
                        .padding(vertical = 3.dp),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else if (deposit && supportedCurrencies.isEmpty() && !isLoadingCurrencies) {
            Text(
                text = "Currency list unavailable.",
                color = Color(0xFFFFC6BE),
                fontSize = 12.sp
            )
            Text(
                text = "Retry loading currencies",
                modifier = Modifier
                    .clickable(onClick = onRetryCurrencies)
                    .padding(vertical = 3.dp),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun KeypadKeyRow(
    amount: String,
    labels: List<String>,
    onAmountChanged: (String) -> Unit,
    isProcessing: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        labels.forEach { label ->
            KeypadKey(
                label = label,
                modifier = Modifier.weight(1f),
                enabled = !isProcessing,
                onClick = { onAmountChanged(amount.appendKey(label)) }
            )
        }
    }
}

@Composable
private fun KeypadKey(
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(KeyBackground.copy(alpha = if (enabled) 1f else 0.5f))
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun String.appendKey(value: String): String {
    if (value == ".") {
        return if (contains('.')) this else if (this == "0") "0." else "$this."
    }

    val fraction = substringAfter('.', "")
    if (contains('.') && fraction.length >= 2) return this
    if (!contains('.') && trimStart('0').length >= 12) return this
    return when {
        this == "0" && value == "00" -> "0"
        this == "0" -> value
        else -> this + value
    }
}

private fun String.currencySymbol(): String = when (uppercase(Locale.ROOT)) {
    "INR" -> "₹"
    "USD" -> "$"
    "EUR" -> "€"
    "GBP" -> "£"
    "JPY" -> "¥"
    "CNY" -> "¥"
    "AUD" -> "A$"
    "CAD" -> "C$"
    else -> this
}

private fun BigDecimal.formatAmount(minimumFractionDigits: Int = 0): String {
    val pattern = if (minimumFractionDigits == 2) "#,##0.00" else "#,##0.##"
    return DecimalFormat(pattern).format(this)
}
