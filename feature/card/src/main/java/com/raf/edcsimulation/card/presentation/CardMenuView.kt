package com.raf.edcsimulation.card.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raf.edcsimulation.card.presentation.viewmodel.CardViewModel
import com.raf.edcsimulation.core.domain.model.CardType
import com.raf.edcsimulation.core.presentation.components.LogoutDialog
import com.raf.edcsimulation.core.presentation.components.SettingsDialog

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CardMenuView(
    viewModel: CardViewModel = hiltViewModel(),
    onCardProcessed: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var dialogState by rememberSaveable {
        mutableStateOf(CardDialogState.NONE)
    }

    BackHandler(uiState.cardData != null) {
        viewModel.onCardTypeChange(null)
    }

    Scaffold(
        topBar = {
            if (uiState.cardData != null) return@Scaffold
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(com.raf.edcsimulation.card.R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AnimatedContent(
                            targetState = dialogState != CardDialogState.SETTINGS,
                            contentAlignment = Alignment.Center,
                        ) { targetState ->
                            if (targetState) {
                                IconButton(
                                    onClick = {
                                        dialogState = CardDialogState.SETTINGS
                                    },
                                    modifier = Modifier
                                        .sharedElement(
                                            sharedContentState = rememberSharedContentState("settings-dialog-key"),
                                            animatedVisibilityScope = this
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "Settings"
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        AnimatedContent(
                            targetState = dialogState != CardDialogState.LOGOUT,
                            contentAlignment = Alignment.Center,
                        ) { targetState ->
                            if (targetState) {
                                IconButton(
                                    onClick = {
                                        dialogState = CardDialogState.LOGOUT
                                    },
                                    modifier = Modifier
                                        .sharedElement(
                                            sharedContentState = rememberSharedContentState("logout-dialog-key"),
                                            animatedVisibilityScope = this
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Logout,
                                        contentDescription = "Logout"
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            /**
             * Menu
             */
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(innerPadding)
            ) {
                Text(
                    text = "Choose Card Type",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                viewModel.listCard.forEach { cardData ->
                    CardMenuItem(
                        modifier = Modifier.weight(1f),
                        cardData = cardData,
                        visible = uiState.cardData == null,
                        onMenuClicked = {
                            viewModel.onCardTypeChange(cardData)
                        }
                    )
                }
            }

            /**
             * Card Simulation
             */
            AnimatedContent(
                targetState = uiState.cardData != null && uiState.cardData?.cardType != CardType.MANUAL,
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) { targetState ->
                if (targetState) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("${uiState.cardData?.cardType}-card-key"),
                                animatedVisibilityScope = this@AnimatedContent
                            )
                            .padding(innerPadding)
                    ) {
                        CardItem(
                            card = uiState.cardData,
                            visible = uiState.cardData?.cardType != CardType.MANUAL,
                            onCardProcessed = {
                                viewModel.processCard(
                                    onCardProcessed = onCardProcessed
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                        )
                        Text(
                            text = "Tap or Swipe Up the Card to Process",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        TextButton(
                            onClick = {
                                viewModel.onCardTypeChange(null)
                            }
                        ) {
                            Text(text = "Cancel", textDecoration = TextDecoration.Underline)
                        }
                    }
                }
            }

            AnimatedContent(
                targetState = uiState.cardData != null && uiState.cardData?.cardType == CardType.MANUAL,
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) { targetState ->
                if (targetState) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("${uiState.cardData?.cardType}-card-key"),
                                animatedVisibilityScope = this@AnimatedContent
                            )
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(innerPadding)
                    ) {
                        Text(
                            text = "Enter Card Number",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        OutlinedTextField(
                            value = viewModel.cardNumber,
                            onValueChange = viewModel::onCardNumberChange,
                            singleLine = true,
                            placeholder = {
                                Text(text = "e.g. 1234567890")
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Number
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        Button(
                            onClick = {
                                viewModel.processManualCard(
                                    onCardProcessed = onCardProcessed
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(text = "Process")
                        }
                        TextButton(
                            onClick = {
                                viewModel.onCardTypeChange(null)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 16.dp)
                        ) {
                            Text(text = "Cancel", textDecoration = TextDecoration.Underline)
                        }
                    }
                }
            }

            /**
             * Processing Card Simulation
             */
            AnimatedVisibility(
                visible = uiState.isLoading && !uiState.message.isNullOrBlank(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Card {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        CircularProgressIndicator()
                        uiState.message?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Dialog
     */
    SettingsDialog(
        isShown = dialogState == CardDialogState.SETTINGS,
        onDismissRequest = {
            dialogState = CardDialogState.NONE
        },
        currentAppSettings = uiState.appSettings,
        onAppSettingsChange = {
            viewModel.setAppSettings(it)
        },
    )
    LogoutDialog(
        isShown = dialogState == CardDialogState.LOGOUT,
        onDismissRequest = {
            dialogState = CardDialogState.NONE
        },
        onLogout = {
            viewModel.logout()
        }
    )
}