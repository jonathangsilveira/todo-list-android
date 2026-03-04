package org.jgsilveira.todolist.android.features.auth.signin

import org.jgsilveira.todolist.android.core.presentation.model.ViewState

internal data class SignInViewState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isSigningIn: Boolean = false
) : ViewState
