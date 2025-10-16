package com.raf.edcsimulation.settlement.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StickyNote2
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raf.edcsimulation.core.presentation.theme.approvedColor
import com.raf.edcsimulation.core.presentation.theme.onApprovedColor
import com.raf.edcsimulation.settlement.presentation.components.SettlementItemCountView
import com.raf.edcsimulation.settlement.presentation.viewmodel.SettlementViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettlementScreen(
    paddingValues: PaddingValues = PaddingValues(),
    settlementDataCount: Int = 0,
    approvedDataCount: Int = 0,
    viewModel: SettlementViewModel = hiltViewModel(),
    onSettledSuccessfully: () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settlement",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding() + 32.dp
                )
                .padding(horizontal = 16.dp)
        ) {
            SettlementItemCountView(
                title = "Settlement Data",
                count = settlementDataCount,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp, max = 200.dp)
            )
            SettlementItemCountView(
                title = "Approved Data",
                count = approvedDataCount,
                containerColor = approvedColor,
                textColor = onApprovedColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp, max = 200.dp)
            )
            Button(
                onClick = viewModel::settleNow,
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator()
                        Text(text = "Loading...")
                        return@Row
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.StickyNote2,
                        contentDescription = "Settle Now"
                    )
                    Text(text = "Settle Now")
                }
            }
        }

        // Success Dialog
        if (uiState.data?.recordsUpdated != null) {
            AlertDialog(
                onDismissRequest = {
                    onSettledSuccessfully()
                    viewModel.resetState()
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onSettledSuccessfully()
                            viewModel.resetState()
                        }
                    ) {
                        Text(text = "Close")
                    }
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success"
                    )
                },
                title = {
                    Text(text = "Success")
                },
                text = {
                    Column {
                        Text(text = uiState.data?.message ?: "")
                        Text(text = "Records Updated: ${uiState.data?.recordsUpdated}")
                    }
                }
            )
        }

        // Error Dialog
        if (uiState.errorMessage != null) {
            AlertDialog(
                onDismissRequest = { viewModel.resetState() },
                confirmButton = {
                    TextButton(
                        onClick = viewModel::resetState
                    ) {
                        Text(text = "Close")
                    }
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                title = {
                    Text(text = "Failed")
                },
                text = {
                    Text(text = uiState.errorMessage ?: "Failed to settle")
                }
            )
        }
    }
}