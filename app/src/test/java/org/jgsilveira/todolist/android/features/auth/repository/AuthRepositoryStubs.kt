package org.jgsilveira.todolist.android.features.auth.repository

import org.jgsilveira.todolist.android.features.auth.domain.model.AuthTokens
import org.jgsilveira.todolist.android.features.auth.domain.model.TokenType
import org.jgsilveira.todolist.android.features.auth.domain.model.SignInForm
import org.jgsilveira.todolist.android.features.auth.domain.model.SignUpForm

internal object AuthRepositoryStubs {

    val authTokens = AuthTokens(
        accessToken = "token",
        refreshToken = "token",
        expiresAt = 0L,
        type = TokenType.BEARER
    )

    val signInForm = SignInForm(
        username = "chablau@chablau.com",
        password = "chablau"
    )

    val signUpForm = SignUpForm(
        fullName = "Chablauzinho",
        email = "chablau@chablau.com",
        password = "chablau"
    )
}