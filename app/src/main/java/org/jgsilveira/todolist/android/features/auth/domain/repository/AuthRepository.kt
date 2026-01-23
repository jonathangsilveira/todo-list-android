package org.jgsilveira.todolist.android.features.auth.domain.repository

import org.jgsilveira.todolist.android.features.auth.domain.model.SignInForm
import org.jgsilveira.todolist.android.features.auth.domain.model.SignUpForm

interface AuthRepository {
    suspend fun signUp(form: SignUpForm)
    suspend fun signIn(form: SignInForm)
    suspend fun signOut()
}