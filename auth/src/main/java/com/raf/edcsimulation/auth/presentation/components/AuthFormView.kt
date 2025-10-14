package com.raf.edcsimulation.auth.presentation.components

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.raf.edcsimulation.auth.presentation.viewmodel.AuthState
import com.raf.edcsimulation.auth.presentation.viewmodel.AuthUiState
import com.raf.edcsimulation.auth.presentation.viewmodel.AuthViewModel
import com.raf.edcsimulation.core.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.AuthFormView(
    modifier: Modifier = Modifier,
    animatedContentScope: AnimatedContentScope,
    uiState: AuthUiState = AuthUiState(),
    title: String = "Login",
    isRegisterForm: Boolean = false,
    viewModel: AuthViewModel = hiltViewModel<AuthViewModel>(),
    ctaLabel: String = "Login",
    onCTA: () -> Unit = {},
    navigationLabel: String = "Register",
    onNavigation: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()

    // Button Validation & Response State
    LaunchedEffect(
        uiState.username,
        uiState.password,
        uiState.confirmPassword
    ) {
        viewModel.validateButton(isRegisterForm)
        viewModel.resetAuthState()
    }

    // Login Change Clear Validation
    LaunchedEffect(isRegisterForm) {
        if (!isRegisterForm) {
            viewModel.resetError()
        }
    }

    // Register Change Validation
    LaunchedEffect(isRegisterForm) {
        if (isRegisterForm) {
            if (uiState.username.isNotBlank()) {
                viewModel.validateUsername(true)
            }
            if (uiState.password.isNotBlank()) {
                viewModel.validatePassword(true)
            }
            if (uiState.confirmPassword.isNotBlank()) {
                viewModel.validateConfirmPassword()
            }
        }
        viewModel.validateButton(isRegisterForm)
    }

    // Register Success
    LaunchedEffect(uiState.registerState) {
        if (uiState.registerState is AuthState.Success && isRegisterForm) {
            Toast.makeText(
                context,
                uiState.registerState.message,
                Toast.LENGTH_SHORT
            ).show()
            onNavigation.invoke()
            viewModel.resetUiState()
        }
    }

    // Login Success
    LaunchedEffect(uiState.loginState) {
        if (uiState.loginState is AuthState.Success && !isRegisterForm) {
            Toast.makeText(
                context,
                uiState.loginState.message,
                Toast.LENGTH_SHORT
            ).show()
            onLoginSuccess.invoke()
            viewModel.resetUiState()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.title).uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("auth-title-key"),
                        animatedVisibilityScope = animatedContentScope
                    )
            )
            OutlinedTextField(
                value = uiState.username,
                onValueChange = {
                    viewModel.onInputChange(username = it)
                    viewModel.validateUsername(isRegisterForm)
                },
                label = {
                    Text(text = "Username")
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions {
                    this.defaultKeyboardAction(
                        imeAction = ImeAction.Next
                    )
                },
                singleLine = true,
                isError = uiState.usernameError,
                supportingText = {
                    AnimatedVisibility(
                        visible = !uiState.usernameErrorMessage.isBlank(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = uiState.usernameErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Username"
                    )
                },
                trailingIcon = {
                    AnimatedVisibility(
                        visible = !uiState.username.isBlank(),
                        enter = scaleIn(),
                        exit = scaleOut()
                    ) {
                        IconButton(
                            onClick = {
                                viewModel.onInputChange(username = "")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 220.dp)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("auth-username-text-field-key"),
                        animatedVisibilityScope = animatedContentScope
                    )
            )
            OutlinedTextField(
                value = uiState.password,
                onValueChange = {
                    viewModel.onInputChange(password = it)
                    viewModel.validatePassword(isRegisterForm)
                    viewModel.validateConfirmPassword()
                },
                label = {
                    Text(text = "Password")
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = if (isRegisterForm) ImeAction.Next else ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions {
                    this.defaultKeyboardAction(
                        imeAction = if (isRegisterForm) ImeAction.Next else ImeAction.Done
                    )
                },
                visualTransformation = if (uiState.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                isError = uiState.passwordError,
                supportingText = {
                    AnimatedVisibility(
                        visible = !uiState.passwordErrorMessage.isBlank(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = uiState.passwordErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = "Password"
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onInputChange(showPassword = !uiState.showPassword)
                        }
                    ) {
                        Icon(
                            imageVector = if (uiState.showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Show/Hide Password"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 220.dp)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("auth-password-text-field-key"),
                        animatedVisibilityScope = animatedContentScope
                    )
            )
            AnimatedVisibility(
                visible = isRegisterForm,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                OutlinedTextField(
                    value = uiState.confirmPassword,
                    onValueChange = {
                        viewModel.onInputChange(confirmPassword = it)
                        viewModel.validateConfirmPassword()
                    },
                    label = {
                        Text(text = "Confirm Password")
                    },
                    visualTransformation = if (uiState.showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions {
                        this.defaultKeyboardAction(
                            imeAction = ImeAction.Done
                        )
                    },
                    isError = uiState.confirmPasswordError,
                    singleLine = true,
                    supportingText = {
                        AnimatedVisibility(
                            visible = !uiState.confirmPasswordErrorMessage.isBlank(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Text(
                                text = uiState.confirmPasswordErrorMessage,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Password,
                            contentDescription = "Confirm Password"
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                viewModel.onInputChange(showConfirmPassword = !uiState.showConfirmPassword)
                            }
                        ) {
                            Icon(
                                imageVector = if (uiState.showConfirmPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Show/Hide Confirm Password"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 220.dp)
                )
            }
            AnimatedVisibility(
                visible = uiState.loginState is AuthState.Error || uiState.registerState is AuthState.Error,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = (uiState.loginState as? AuthState.Error)?.message
                        ?: (uiState.registerState as? AuthState.Error)?.message
                        ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(top = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onCTA,
                enabled = uiState.isButtonEnabled && uiState.loginState !is AuthState.Loading &&
                        uiState.registerState !is AuthState.Loading &&
                        uiState.loginState !is AuthState.Error && uiState.registerState !is AuthState.Error,
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 220.dp)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("auth-cta-button-key"),
                        animatedVisibilityScope = animatedContentScope
                    )
            ) {
                AnimatedContent(
                    targetState = uiState.loginState is AuthState.Loading || uiState.registerState is AuthState.Loading,
                    modifier = Modifier
                ) { targetState ->
                    if (targetState) {
                        CircularProgressIndicator()
                    } else {
                        Text(
                            text = ctaLabel,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                modifier = Modifier
                    .fillMaxWidth()
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("auth-or-label-key"),
                        animatedVisibilityScope = animatedContentScope
                    )
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    text = "OR",
                    style = MaterialTheme.typography.titleMedium,
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }
            TextButton(
                onClick = {
                    onNavigation.invoke()
                },
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("auth-navigation-button-key"),
                        animatedVisibilityScope = animatedContentScope
                    )
            ) {
                Text(
                    text = navigationLabel,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}