package org.jgsilveira.todolist.android.features.auth.data.repository

import org.jgsilveira.todolist.android.features.auth.data.provider.AuthTokensStore
import org.jgsilveira.todolist.android.features.auth.data.client.AuthClient
import org.jgsilveira.todolist.android.features.auth.domain.model.SignInForm
import org.jgsilveira.todolist.android.features.auth.domain.model.SignUpForm
import org.jgsilveira.todolist.android.features.auth.domain.repository.AuthRepository

internal class AuthRepositoryImpl(
    private val authTokensStore: AuthTokensStore,
    private val authClient: AuthClient
) : AuthRepository {

    override suspend fun signUp(form: SignUpForm) {
        authClient.signUp(form)
    }

    override suspend fun signIn(form: SignInForm) {
        val authTokens = authClient.signIn(form)
        authTokensStore.storeAuthTokens(authTokens)
    }

    override suspend fun signOut() {
        authClient.signOut()
        authTokensStore.clearAuthTokens()
    }
}