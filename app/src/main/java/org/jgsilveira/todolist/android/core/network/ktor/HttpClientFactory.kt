package org.jgsilveira.todolist.android.core.network.ktor

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createKtorHttpClient(
    baseUrl: String,
    bearerAuthProvider: KtorBearerAuthProvider
): HttpClient {
    return HttpClient(OkHttp) {
        expectSuccess = true
        defaultRequest {
            url(urlString = baseUrl)
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 2)
            exponentialDelay()
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
        install(Auth) {
            bearer {
                loadTokens {
                    bearerAuthProvider.getAuthTokens()
                }
                refreshTokens {
                    bearerAuthProvider.refreshTokens(params = this)
                }
            }
        }
        install(plugin = ContentNegotiation) {
            json(
                Json {
                    encodeDefaults = true
                    isLenient = true
                    coerceInputValues = true
                    ignoreUnknownKeys = true
                }
            )
        }
    }
}
