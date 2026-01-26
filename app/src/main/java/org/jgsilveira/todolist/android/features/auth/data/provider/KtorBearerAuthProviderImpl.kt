package org.jgsilveira.todolist.android.features.auth.data.provider

import io.ktor.client.call.body
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import io.ktor.client.request.post
import io.ktor.http.appendPathSegments
import org.jgsilveira.todolist.android.core.network.ktor.KtorBearerAuthProvider
import org.jgsilveira.todolist.android.features.auth.data.model.AuthTokenResponse
import org.jgsilveira.todolist.android.features.auth.domain.model.AuthTokens
import org.jgsilveira.todolist.android.features.auth.domain.model.TokenType
import org.jgsilveira.todolist.android.features.auth.data.model.toBearerTokens

internal class KtorBearerAuthProviderImpl(
    private val authTokensStore: AuthTokensStore
) : KtorBearerAuthProvider {
    override suspend fun getAuthTokens(): BearerTokens? {
        return authTokensStore.getAuthTokens()?.toBearerTokens()
    }

    override suspend fun refreshTokens(params: RefreshTokensParams): BearerTokens? {
        val currentTokens = params.oldTokens ?: return null
        val response = params.client.post {
            url {
                appendPathSegments(
                    "/auth/v1/token/refresh",
                    currentTokens.refreshToken.orEmpty()
                )
            }
        }
        val body = response.body<AuthTokenResponse>()
        val authTokens = AuthTokens(
            accessToken = body.accessToken,
            refreshToken = body.refreshToken,
            type = TokenType.BEARER,
            expiresAt = body.expiresAt
        )
        authTokensStore.storeAuthTokens(authTokens)
        return authTokens.toBearerTokens()
    }
}