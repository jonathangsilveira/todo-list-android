package org.jgsilveira.todolist.android.features.auth.data.service

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.authProviders
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.delete
import io.ktor.client.request.forms.submitForm
import io.ktor.http.appendPathSegments
import io.ktor.http.parameters
import org.jgsilveira.todolist.android.features.auth.data.model.AuthTokenResponse
import org.jgsilveira.todolist.android.features.auth.data.model.AuthTokens
import org.jgsilveira.todolist.android.features.auth.data.model.TokenType
import org.jgsilveira.todolist.android.features.auth.domain.model.SignInForm
import org.jgsilveira.todolist.android.features.auth.domain.model.SignUpForm

private const val V1_AUTH_PATH = "/auth/v1"

internal interface AuthApiService {
    suspend fun signUp(form: SignUpForm)
    suspend fun signIn(form: SignInForm): AuthTokens
    suspend fun signOut()
}

internal class KtorAuthApiService(
    private val httpClient: HttpClient
) : AuthApiService {

    override suspend fun signUp(form: SignUpForm) {
        httpClient.submitForm(
            formParameters = parameters {
                append("full_name", form.fullName)
                append("email", form.email)
                append("password", form.password)
            }
        ) {
            url {
                appendPathSegments(V1_AUTH_PATH, "signup")
            }
        }
    }

    override suspend fun signIn(form: SignInForm): AuthTokens {
        val response = httpClient.submitForm(
            formParameters = parameters {
                set("username", form.username)
                set("password", form.password)
                set("grant_type", form.grantType)
            }
        ) {
            url {
                appendPathSegments(V1_AUTH_PATH, "signin")
            }
        }
        val body = response.body<AuthTokenResponse>()
        return AuthTokens(
            accessToken = body.accessToken,
            refreshToken = body.refreshToken,
            type = TokenType.valueOf(body.type.uppercase()),
            expiresAt = body.expiresAt
        )
    }

    override suspend fun signOut() {
        runCatching {
            httpClient.delete {
                url {
                    appendPathSegments(V1_AUTH_PATH, "signout")
                }
            }
        }.onFailure {
            Log.d("AUTH_API_SERVICE", "Failed to signing-out", it)
        }
        clearCachedClientTokens()
    }

    private fun clearCachedClientTokens() {
        httpClient.authProviders
            .filterIsInstance<BearerAuthProvider>()
            .forEach { authProvider ->
                authProvider.clearToken()
            }
    }
}