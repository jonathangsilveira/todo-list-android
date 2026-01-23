package org.jgsilveira.todolist.android.features.auth.domain.model

data class SignInForm(
    val username: String,
    val password: String,
    val grantType: String = "password"
)
