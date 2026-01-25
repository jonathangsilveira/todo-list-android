package org.jgsilveira.todolist.android.features.auth.service

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val APPLICATION_JSON = "application/json"

internal object AuthClientStubs {

    val successResponseSignUpEngine = MockEngine {
        respond(
            content = "{\"message\": \"User created!\"}",
            status = HttpStatusCode.Created,
            headers = headersOf(
                HttpHeaders.ContentType,
                APPLICATION_JSON
            )
        )
    }

    val conflictResponseSignUpEngine = MockEngine {
        respond(
            content = "{\"message\": \"User with e-mail chablau@chablau.com already exists!\"}",
            status = HttpStatusCode.Conflict,
            headers = headersOf(
                HttpHeaders.ContentType,
                APPLICATION_JSON
            )
        )
    }

    val successResponseSignInEngine = MockEngine {
        respond(
            content = "{\"refresh_token\": \"token\",\"access_token\": \"token\",\"expires_at\": 0,\"token_type\": \"Bearer\"}",
            status = HttpStatusCode.OK,
            headers = headersOf(
                HttpHeaders.ContentType,
                APPLICATION_JSON
            )
        )
    }

    val notFoundResponseSignInEngine = MockEngine {
        respond(
            content = "{\"message\": \"User with email chablau@chablau.com was not found.\"}",
            status = HttpStatusCode.NotFound,
            headers = headersOf(
                HttpHeaders.ContentType,
                APPLICATION_JSON
            )
        )
    }

    val noContentResponseSignOutEngine = MockEngine {
        respond(
            content = "",
            status = HttpStatusCode.NoContent,
            headers = headersOf(
                HttpHeaders.ContentType,
                APPLICATION_JSON
            )
        )
    }

    val unauthorizedResponseSignOutEngine = MockEngine {
        respond(
            content = "{\"message\": \"Token expired\"}",
            status = HttpStatusCode.Unauthorized,
            headers = headersOf(
                HttpHeaders.ContentType,
                APPLICATION_JSON
            )
        )
    }

    fun createKtorClient(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine = engine) {
            expectSuccess = true
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
}