package org.jgsilveira.todolist.android.features.auth.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.jgsilveira.todolist.android.ktor.mock.createHttpClient

internal object AuthClientStubs {

    val json = Json {
        encodeDefaults = true
        isLenient = true
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    fun createHttpClient(engine: HttpClientEngine): HttpClient {
        return createHttpClient(engine) {
            expectSuccess = true
            install(plugin = ContentNegotiation) {
                json(json)
            }
        }
    }
}