package com.raf.edcsimulation.auth.presentation.screens

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.raf.edcsimulation.auth.presentation.components.AuthFormView
import com.raf.edcsimulation.auth.presentation.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.LoginScreen(
    animatedContentScope: AnimatedContentScope,
    viewModel: AuthViewModel = hiltViewModel<AuthViewModel>(),
    onNavigateToRegister: () -> Unit = {}
) {
    AuthFormView(
        animatedContentScope = animatedContentScope,
        title = "Login",
        isRegisterForm = false,
        viewModel = viewModel,
        ctaLabel = "Login",
        onCTA = {
            viewModel.login()
        },
        navigationLabel = "Register",
        onNavigation = {
            onNavigateToRegister.invoke()
            viewModel.resetUserInputData()
        }
    )
}