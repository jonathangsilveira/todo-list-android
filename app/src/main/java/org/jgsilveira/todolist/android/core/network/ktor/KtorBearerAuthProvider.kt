package org.jgsilveira.todolist.android.core.network.ktor

import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams

interface KtorBearerAuthProvider {
    suspend fun getAuthTokens(): BearerTokens?
    suspend fun refreshTokens(params: RefreshTokensParams): BearerTokens?
}