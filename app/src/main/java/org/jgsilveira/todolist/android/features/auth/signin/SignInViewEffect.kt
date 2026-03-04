package org.jgsilveira.todolist.android.features.auth.signin

import org.jgsilveira.todolist.android.core.presentation.model.ViewEffect

internal sealed interface SignInViewEffect: ViewEffect {
    data object NavToSignUp: SignInViewEffect
    data object NavToHome: SignInViewEffect
}