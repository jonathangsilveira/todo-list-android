package org.jgsilveira.todolist.android.ktor.mock

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.serialization.json.Json

internal fun mockEngineRespondingWith(
    content: String,
    status: HttpStatusCode,
    headers: Headers = headersOf()
): MockEngine = MockEngine {
    respond(
        content = content,
        status = status,
        headers = headers
    )
}

internal fun mockEngineRespondingJson(
    status: HttpStatusCode,
    content: String = ""
) = mockEngineRespondingWith(
    content = content,
    status = status,
    headers = headersOf(
        HttpHeaders.ContentType,
        ContentType.Application.Json.toString()
    )
)

internal inline fun <reified T> mockEngineRespondingJson(
    content: T,
    status: HttpStatusCode,
    json: Json = Json.Default
) = mockEngineRespondingWith(
    content = json.encodeToString(content),
    status = status,
    headers = headersOf(
        HttpHeaders.ContentType,
        ContentType.Application.Json.toString()
    )
)

internal fun mockEngineRespondingSuccessJson(
    content: String = ""
) = mockEngineRespondingJson(
    content = content,
    status = HttpStatusCode.OK
)

internal fun mockEngineRespondingClientErrorJson(
    content: String = ""
) = mockEngineRespondingJson(
    content = content,
    status = HttpStatusCode.BadRequest
)

internal fun mockEngineRespondingServerErrorJson(
    content: String = ""
) = mockEngineRespondingJson(
    content = content,
    status = HttpStatusCode.BadGateway
)

internal inline fun <reified T> mockEngineRespondingSuccessJson(
    content: T,
    json: Json = Json.Default
) = mockEngineRespondingJson(
    content = content,
    status = HttpStatusCode.OK,
    json = json
)

internal inline fun <reified T> mockEngineRespondingClientErrorJson(
    content: T,
    json: Json = Json.Default
) = mockEngineRespondingJson(
    content = content,
    status = HttpStatusCode.BadRequest,
    json = json
)

internal inline fun <reified T> mockEngineRespondingServerErrorJson(
    content: T,
    json: Json = Json.Default
) = mockEngineRespondingJson(
    content = content,
    status = HttpStatusCode.InternalServerError,
    json = json
)

internal val defaultMockEngine: MockEngine = mockEngineRespondingSuccessJson()