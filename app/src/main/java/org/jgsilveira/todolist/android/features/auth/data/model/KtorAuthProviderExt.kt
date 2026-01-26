package org.jgsilveira.todolist.android.features.auth.data.model

import io.ktor.client.plugins.auth.providers.BearerTokens
import org.jgsilveira.todolist.android.features.auth.domain.model.AuthTokens

internal fun AuthTokens.toBearerTokens(): BearerTokens {
    return BearerTokens(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )
}