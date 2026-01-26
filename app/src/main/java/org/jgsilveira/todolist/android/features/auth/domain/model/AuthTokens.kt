package org.jgsilveira.todolist.android.features.auth.domain.model

enum class TokenType(val value: String) {
    BEARER(value = "Bearer")
}

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Long,
    val type: TokenType = TokenType.BEARER
)