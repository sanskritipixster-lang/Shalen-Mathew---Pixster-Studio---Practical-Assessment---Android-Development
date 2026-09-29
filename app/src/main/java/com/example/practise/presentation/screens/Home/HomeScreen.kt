package com.example.practise.presentation.screens.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.practise.presentation.screens.Home.util.HomeHeader

private val HomeBlue = Color(0xFF087FF5)
private val HistoryBackground = Color(0xFFF4F6F8)
private val PrimaryText = Color(0xFF263B50)
private val MutedText = Color(0xFF929BA4)
private val InflowColor = Color(0xFF59D876)
private val InflowBackground = Color(0xFFE7F9EB)
private val OutflowColor = Color(0xFFFF8B7C)
private val OutflowBackground = Color(0xFFFFEFEC)
private val KeypadBackground = Color(0xFF092A45)
private val KeyBackground = Color(0xFF29435A)
private val ConfirmGreen = Color(0xFF36D66B)
private val DeleteRed = Color(0xFFFF5947)

private data class CurrencyOption(val code: String, val symbol: String)

private val depositCurrencies = listOf(
    CurrencyOption("USD", "$"),
    CurrencyOption("INR", "₹"),
    CurrencyOption("EUR", "€"),
    CurrencyOption("GBP", "£")
)

private data class Transaction(
    val id: String,
    val date: String,
    val amount: String,
    val isInflow: Boolean,
    val sourceAmount: String? = null
)

private val transactions = listOf(
    Transaction("ID21W234R3", "12 Apr 2024", "$24", isInflow = true),
    Transaction("ID213EE30", "11 Apr 2024", "₹ 11,452", isInflow = false, sourceAmount = "$12"),
    Transaction("ID21334R3", "2 Apr 2024", "$46", isInflow = true),
    Transaction("ID2132R42", "23 Mar 2024", "$21", isInflow = true),
    Transaction("ID213EE30", "11 Mar 2024", "₹ 8023", isInflow = false, sourceAmount = "$32"),
    Transaction("ID21W234R3", "8 Mar 2024", "$24", isInflow = true),
    Transaction("ID2132R42", "2 Mar 2024", "₹ 7,895", isInflow = false, sourceAmount = "$18"),
    Transaction("ID21334R3", "24 Feb 2024", "$46", isInflow = true),
    Transaction("ID21W234R3", "12 Apr 2024", "$24", isInflow = true),
    Transaction("ID213EE30", "11 Apr 2024", "₹ 11,452", isInflow = false, sourceAmount = "$12"),
    Transaction("ID21334R3", "2 Apr 2024", "$46", isInflow = true),
    Transaction("ID2132R42", "23 Mar 2024", "$21", isInflow = true),
    Transaction("ID213EE30", "11 Mar 2024", "₹ 8023", isInflow = false, sourceAmount = "$32"),
    Transaction("ID21W234R3", "8 Mar 2024", "$24", isInflow = true),
    Transaction("ID2132R42", "2 Mar 2024", "₹ 7,895", isInflow = false, sourceAmount = "$18"),
    Transaction("ID21334R3", "24 Feb 2024", "$46", isInflow = true)

)

@Composable
fun HomeScreen(
    navHost: NavHostController,
    paddingValues: PaddingValues
) {
    var showDepositKeypad by remember { mutableStateOf(false) }
    var depositAmount by remember { mutableStateOf("0") }
    var selectedCurrency by remember { mutableStateOf(depositCurrencies.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HomeBlue)
    ) {
        HomeHeader(
            balance = "₹ 24,224.90",
            onWithdrawClick = {
                // TODO
            },
            onDepositClick = {
                showDepositKeypad = true
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
            items(transactions) { transaction ->
                TransactionCard(transaction)
            }
        }
    }

    if (showDepositKeypad) {
        Dialog(
            onDismissRequest = { showDepositKeypad = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            DepositKeypad(
                amount = depositAmount,
                currency = selectedCurrency,
                onCurrencySelected = { selectedCurrency = it },
                onAmountChanged = { depositAmount = it },
                onConfirm = { showDepositKeypad = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 360.dp)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun DepositKeypad(
    amount: String,
    currency: CurrencyOption,
    onCurrencySelected: (CurrencyOption) -> Unit,
    onAmountChanged: (String) -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currencyMenuExpanded by remember { mutableStateOf(false) }

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
                    .clickable { currencyMenuExpanded = true }
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${currency.symbol}  ▾",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                DropdownMenu(
                    expanded = currencyMenuExpanded,
                    onDismissRequest = { currencyMenuExpanded = false }
                ) {
                    depositCurrencies.forEach { option ->
                        DropdownMenuItem(
                            text = { Text("${option.symbol}  ${option.code}") },
                            onClick = {
                                onCurrencySelected(option)
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
                KeypadKeyRow(amount, listOf("7", "8", "9"), onAmountChanged)
                KeypadKeyRow(amount, listOf("4", "5", "6"), onAmountChanged)
            }
            KeypadKey(
                label = "AC",
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp),
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
                    onClick = { appendAmount(amount, digit, onAmountChanged) }
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DeleteRed)
                    .clickable {
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
                    onClick = { appendAmount(amount, value, onAmountChanged) }
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .clip(CircleShape)
                    .background(ConfirmGreen)
                    .clickable {
                        if ((amount.toDoubleOrNull() ?: 0.0) > 0.0) {
                            onConfirm()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Confirm deposit amount",
                    modifier = Modifier.size(22.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun KeypadKeyRow(
    amount: String,
    labels: List<String>,
    onAmountChanged: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        labels.forEach { label ->
            KeypadKey(
                label = label,
                modifier = Modifier.weight(1f),
                onClick = { appendAmount(amount, label, onAmountChanged) }
            )
        }
    }
}

@Composable
private fun KeypadKey(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(KeyBackground)
            .clickable(onClick = onClick),
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

private fun appendAmount(
    currentAmount: String,
    value: String,
    onAmountChanged: (String) -> Unit
) {
    if (value == ".") {
        if (!currentAmount.contains('.')) {
            onAmountChanged("$currentAmount.")
        }
        return
    }

    val fractionalDigits = currentAmount.substringAfter('.', "")
    if (currentAmount.contains('.') && fractionalDigits.length >= 2) return

    val updatedAmount = when {
        currentAmount == "0" && value == "00" -> "0"
        currentAmount == "0" -> value
        else -> currentAmount + value
    }
    onAmountChanged(updatedAmount)
}

@Composable
private fun TransactionCard(transaction: Transaction) {
    val iconColor = if (transaction.isInflow) InflowColor else OutflowColor
    val iconBackground = if (transaction.isInflow) InflowBackground else OutflowBackground

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
            .shadow(2.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(31.dp)
                .clip(CircleShape)
                .background(iconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = if (transaction.isInflow) "Money received" else "Money sent",
                modifier = Modifier.size(16.dp),
                tint = iconColor
            )
        }

        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.id,
                color = PrimaryText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 15.sp
            )
            Text(
                text = transaction.date,
                color = MutedText,
                fontSize = 9.sp,
                lineHeight = 12.sp
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            transaction.sourceAmount?.let { sourceAmount ->
                Text(
                    text = sourceAmount,
                    color = MutedText,
                    fontSize = 9.sp,
                    lineHeight = 12.sp
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (transaction.sourceAmount != null) {
                    Text(
                        text = "⇄",
                        color = MutedText,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                }
                Text(
                    text = transaction.amount,
                    color = PrimaryText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
