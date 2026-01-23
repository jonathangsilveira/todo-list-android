package org.jgsilveira.todolist.android.features.auth.data.provider

import org.jgsilveira.todolist.android.core.keystore.KeyValueStore
import org.jgsilveira.todolist.android.core.keystore.get
import org.jgsilveira.todolist.android.core.keystore.put
import org.jgsilveira.todolist.android.features.auth.data.model.AuthTokens
import org.jgsilveira.todolist.android.features.auth.data.model.TokenType

private const val ACCESS_TOKEN_KEY = "access_token"
private const val REFRESH_TOKEN_KEY = "refresh_token"
private const val ACCESS_TOKEN_EXPIRES_AT_KEY = "access_token_expires_at"

interface AuthTokensStore {
    suspend fun getAuthTokens(): AuthTokens?
    suspend fun storeAuthTokens(authTokens: AuthTokens)
    suspend fun clearAuthTokens()
}

internal class AuthTokensStoreImpl(
    private val keyValueStore: KeyValueStore
) : AuthTokensStore {
    override suspend fun getAuthTokens(): AuthTokens? {
        return with(keyValueStore) {
            val accessToken = get(ACCESS_TOKEN_KEY) ?: return@with null
            val refreshToken = get(REFRESH_TOKEN_KEY) ?: return@with null
            val expiresAt = get(ACCESS_TOKEN_EXPIRES_AT_KEY, 0L)
            AuthTokens(
                accessToken = accessToken,
                refreshToken = refreshToken,
                type = TokenType.BEARER,
                expiresAt = expiresAt
            )
        }
    }

    override suspend fun storeAuthTokens(authTokens: AuthTokens) {
        with(keyValueStore) {
            put(ACCESS_TOKEN_KEY, authTokens.accessToken)
            put(REFRESH_TOKEN_KEY, authTokens.refreshToken)
            put(ACCESS_TOKEN_EXPIRES_AT_KEY, authTokens.expiresAt)
        }
    }

    override suspend fun clearAuthTokens() {
        with(keyValueStore) {
            remove(ACCESS_TOKEN_KEY)
            remove(REFRESH_TOKEN_KEY)
            remove(ACCESS_TOKEN_EXPIRES_AT_KEY)
        }
    }
}