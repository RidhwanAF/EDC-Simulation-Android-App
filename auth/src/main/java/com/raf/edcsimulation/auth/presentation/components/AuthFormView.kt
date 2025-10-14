package com.raf.edcsimulation.auth.presentation.components

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.raf.edcsimulation.auth.presentation.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.AuthFormView(
    modifier: Modifier = Modifier,
    animatedContentScope: AnimatedContentScope,
    title: String = "Login",
    isRegisterForm: Boolean = false,
    viewModel: AuthViewModel = hiltViewModel<AuthViewModel>(),
    ctaLabel: String = "Login",
    onCTA: () -> Unit = {},
    navigationLabel: String = "Register",
    onNavigation: () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(com.raf.edcsimulation.core.R.string.app_name).uppercase(),
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
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
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
                value = viewModel.email,
                onValueChange = {
                    viewModel.onEmailChange(it)
                },
                label = {
                    Text(text = "Email")
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions {
                    this.defaultKeyboardAction(
                        imeAction = ImeAction.Next
                    )
                },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email"
                    )
                },
                trailingIcon = {
                    AnimatedVisibility(
                        visible = !viewModel.email.isBlank(),
                        enter = scaleIn(),
                        exit = scaleOut()
                    ) {
                        IconButton(
                            onClick = {
                                viewModel.onEmailChange("")
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
                        sharedContentState = rememberSharedContentState("auth-email-text-field-key"),
                        animatedVisibilityScope = animatedContentScope
                    )
            )
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = {
                    viewModel.onPasswordChange(it)
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
                visualTransformation = if (viewModel.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = "Password"
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onShowPasswordChange(!viewModel.showPassword)
                        }
                    ) {
                        Icon(
                            imageVector = if (viewModel.showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
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
                    value = viewModel.confirmPassword,
                    onValueChange = {
                        viewModel.onConfirmPasswordChange(it)
                    },
                    label = {
                        Text(text = "Confirm Password")
                    },
                    visualTransformation = if (viewModel.showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions {
                        this.defaultKeyboardAction(
                            imeAction = ImeAction.Done
                        )
                    },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Password,
                            contentDescription = "Confirm Password"
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                viewModel.onShowConfirmPasswordChange(!viewModel.showConfirmPassword)
                            }
                        ) {
                            Icon(
                                imageVector = if (viewModel.showConfirmPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Show/Hide Confirm Password"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 220.dp)
                )
            }
            Button(
                onClick = onCTA,
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 220.dp)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("auth-cta-button-key"),
                        animatedVisibilityScope = animatedContentScope
                    )
            ) {
                Text(
                    text = ctaLabel,
                    style = MaterialTheme.typography.titleMedium,
                )
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
                    viewModel.resetUserInputData()
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