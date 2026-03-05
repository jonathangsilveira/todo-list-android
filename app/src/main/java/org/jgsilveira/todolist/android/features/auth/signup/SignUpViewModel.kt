package org.jgsilveira.todolist.android.features.auth.signup

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.jgsilveira.todolist.android.R
import org.jgsilveira.todolist.android.core.presentation.mvvm.MVVMViewModel
import org.jgsilveira.todolist.android.features.auth.domain.model.SignUpForm
import org.jgsilveira.todolist.android.features.auth.domain.usecase.SignUpUseCase

internal class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
): MVVMViewModel<SignUpViewState, SignUpViewEffect>(SignUpViewState()) {
    fun updateFullName(text: String) {
        setState { it.copy(fullName = text) }
    }

    fun updateEmailAddress(text: String) {
        setState { it.copy(emailAddress = text) }
    }

    fun updatePassword(text: String) {
        setState { it.copy(passwordPlainText = text) }
    }

    fun updatePasswordVisibility(isVisible: Boolean) {
        setState { it.copy(isPasswordVisible = isVisible) }
    }

    fun signUp() {
        viewModelScope.launch {
            setState { it.copy(isSigningUp = true) }
            val currentState = viewState.value
            val signUpForm = SignUpForm(
                fullName = currentState.fullName,
                email = currentState.emailAddress,
                password = currentState.passwordPlainText
            )
            signUpUseCase(signUpForm)
                .onFailure { _ ->
                    setState { it.copy(isSigningUp = false) }
                    sendEffect(
                        SignUpViewEffect.ShowSnackBar(
                            stringResId = R.string.generic_error_message
                        )
                    )
                }.onSuccess {
                    setState { it.copy(isSigningUp = false) }
                    sendEffect(SignUpViewEffect.NavToSignIn)
                }
        }
    }
}