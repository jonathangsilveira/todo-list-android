package org.jgsilveira.todolist.android.features.auth.domain.model

data class SignUpForm(
    val fullName: String,
    val email: String,
    val password: String,
)
