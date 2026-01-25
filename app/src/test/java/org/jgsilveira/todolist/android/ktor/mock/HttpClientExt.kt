package org.jgsilveira.todolist.android.ktor.mock

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine

internal fun createHttpClient(
    engine: HttpClientEngine,
    clientConfigBlock: HttpClientConfig<*>.() -> Unit = {}
): HttpClient {
    return HttpClient(engine = engine) {
        clientConfigBlock()
    }
}

internal fun defaultHttpClient() = createHttpClient(
    engine = defaultMockEngine
)