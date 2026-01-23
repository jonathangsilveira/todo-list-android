package org.jgsilveira.todolist.android.features.auth.data.model

import io.ktor.client.plugins.auth.providers.BearerTokens

internal fun AuthTokens.toBearerTokens(): BearerTokens {
    return BearerTokens(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )
}