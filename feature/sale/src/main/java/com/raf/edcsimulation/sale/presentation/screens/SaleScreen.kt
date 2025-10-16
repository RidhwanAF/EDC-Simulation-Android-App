package com.raf.edcsimulation.sale.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raf.edcsimulation.sale.presentation.viewmodel.SaleViewModel

@Composable
fun SaleScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: SaleViewModel = hiltViewModel(),
    cardNumber: String? = null,
    onDismiss: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(cardNumber) {
        if (cardNumber != null) {
            viewModel.onSaleInputUpdate(cardNumber = cardNumber)
        }
    }

    BackHandler {
        onDismiss()
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Sale Form",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        OutlinedTextField(
            value = viewModel.cardNumber,
            onValueChange = {},
            singleLine = true,
            readOnly = true,
            label = {
                Text(text = "Card Number")
            },
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 220.dp)
        )
        OutlinedTextField(
            value = viewModel.merchantId,
            onValueChange = {
                viewModel.onSaleInputUpdate(merchantId = it)
            },
            singleLine = true,
            label = {
                Text(text = "Merchant ID")
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 220.dp)
        )
        OutlinedTextField(
            value = viewModel.amount,
            onValueChange = {
                if (it.isDigitsOnly()) {
                    viewModel.onSaleInputUpdate(amount = it)
                }
            },
            singleLine = true,
            label = {
                Text(text = "Amount")
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 220.dp)
        )
        OutlinedTextField(
            value = viewModel.description,
            onValueChange = {
                viewModel.onSaleInputUpdate(description = it)
            },
            singleLine = true,
            label = {
                Text(text = "Description (Optional)")
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions {
                this.defaultKeyboardAction(imeAction = ImeAction.Done)
            },
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 220.dp)
        )
        Button(
            onClick = {
                viewModel.processSale()
            },
            enabled = viewModel.cardNumber.isNotEmpty() && viewModel.merchantId.isNotEmpty() && viewModel.amount.isNotEmpty() && !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 220.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
                return@Button
            }
            Text(
                text = "Process Sale"
            )
        }
        TextButton(
            onClick = onDismiss
        ) {
            Text(
                text = "Cancel"
            )
        }

        // Success Dialog
        uiState.saleData?.let { saleData ->
            AlertDialog(
                onDismissRequest = {
                    onDismiss.invoke()
                    viewModel.resetSaleState()
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Transaction Success"
                    )
                },
                title = {
                    Text(text = "Transaction Success")
                },
                text = {
                    Column {
                        Text(text = "Status: ${saleData.status}")
                        Text(text = "Transaction ID: ${saleData.transactionId}")
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDismiss.invoke()
                            viewModel.resetSaleState()
                        }
                    ) {
                        Text(text = "OK")
                    }
                }
            )
        }

        // Failed Dialog
        uiState.errorMessage?.let { message ->
            AlertDialog(
                onDismissRequest = {
                    viewModel.resetSaleState()
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Transaction Failed",
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                title = {
                    Text(text = "Transaction Failed")
                },
                text = {
                    Text(text = message)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.resetSaleState()
                        }
                    ) {
                        Text(text = "Close")
                    }
                }
            )
        }
    }
}