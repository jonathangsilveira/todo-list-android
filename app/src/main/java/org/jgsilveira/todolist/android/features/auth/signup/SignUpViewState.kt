package org.jgsilveira.todolist.android.features.auth.signup

import org.jgsilveira.todolist.android.core.presentation.model.ViewState

internal data class SignUpViewState(
    val fullName: String = "",
    val emailAddress: String = "",
    val passwordPlainText: String = "",
    val isPasswordVisible: Boolean = false,
    val isSigningUp: Boolean = false
) : ViewState