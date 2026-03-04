package org.jgsilveira.todolist.android.features.auth.signup

import org.jgsilveira.todolist.android.core.presentation.model.ViewEffect

internal sealed interface SignUpViewEffect: ViewEffect {
    data object NavToSignIn: SignUpViewEffect
    data class ShowSnackBar(val stringResId: Int): SignUpViewEffect
}