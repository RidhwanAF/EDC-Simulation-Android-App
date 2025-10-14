package com.raf.edcsimulation.auth.presentation.screens

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raf.edcsimulation.auth.presentation.components.AuthFormView
import com.raf.edcsimulation.auth.presentation.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RegisterScreen(
    animatedContentScope: AnimatedContentScope,
    viewModel: AuthViewModel = hiltViewModel<AuthViewModel>(),
    onNavigateToLogin: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AuthFormView(
        animatedContentScope = animatedContentScope,
        uiState = uiState,
        title = "Register",
        isRegisterForm = true,
        viewModel = viewModel,
        ctaLabel = "Register",
        onCTA = {
            viewModel.register()
        },
        navigationLabel = "Login",
        onNavigation = {
            onNavigateToLogin.invoke()
        }
    )
}