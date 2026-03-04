package org.jgsilveira.todolist.android.features.auth.signin

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.jgsilveira.todolist.android.core.presentation.mvvm.MVVMViewModel
import org.jgsilveira.todolist.android.features.auth.domain.model.SignInForm
import org.jgsilveira.todolist.android.features.auth.domain.usecase.SignInUseCase

private const val TAG = "SIGN_IN_PRESENTATION"

internal class SignInViewModel(
    private val signInUseCase: SignInUseCase
): MVVMViewModel<SignInViewState, SignInViewEffect>(SignInViewState()) {
    fun updateEmail(email: String) {
        setState { it.copy(email = email) }
    }

    fun updatePassword(password: String) {
        setState { it.copy(password = password) }
    }

    fun updatePasswordVisibility(isVisible: Boolean) {
        setState { it.copy(isPasswordVisible = isVisible) }
    }

    fun signIn() {
        viewModelScope.launch {
            setState { it.copy(isSigningIn = true) }
            val form = with(viewState.value) {
                SignInForm(
                    username = email,
                    password = password
                )
            }
            signInUseCase(form)
                .onFailure { throwable ->
                    setState { it.copy(isSigningIn = false) }
                    Log.d(TAG, "Error signing-in", throwable)
                }.onSuccess {
                    setState { it.copy(isSigningIn = false) }
                    sendEffect(SignInViewEffect.NavToHome)
                }
        }
    }

    fun signUp() {
        sendEffect(SignInViewEffect.NavToSignUp)
    }
}