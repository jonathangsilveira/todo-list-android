package org.jgsilveira.todolist.android.features.auth.service

import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.jgsilveira.todolist.android.features.auth.data.model.AuthTokenResponse
import org.jgsilveira.todolist.android.features.auth.data.model.MessageResponse
import org.jgsilveira.todolist.android.features.auth.data.model.TokenType
import org.jgsilveira.todolist.android.features.auth.data.client.KtorAuthClient
import org.jgsilveira.todolist.android.features.auth.domain.model.SignInForm
import org.jgsilveira.todolist.android.features.auth.domain.model.SignUpForm
import org.jgsilveira.todolist.android.ktor.mock.mockEngineRespondingJson
import org.jgsilveira.todolist.android.ktor.mock.mockEngineRespondingSuccessJson
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
internal class AuthClientTest {
    private val testDispatcher = StandardTestDispatcher(
        scheduler = TestCoroutineScheduler()
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun signUpShouldReturnOKWhenFormIsCorrect() = runTest {
        // Given
        val engine = mockEngineRespondingSuccessJson(
            content = MessageResponse(message = "User created!"),
            json = AuthClientStubs.json
        )
        val authClient = KtorAuthClient(
            httpClient = AuthClientStubs.createHttpClient(engine)
        )
        val form = SignUpForm(
            fullName = "Chablau",
            email = "chablau@chablau.com",
            password = "chablau"
        )

        // When
        authClient.signUp(form)
    }

    @Test
    fun signUpShouldReturnConflictWhenEmailAlreadyExists() = runTest {
        // Given
        val engine = mockEngineRespondingJson(
            status = HttpStatusCode.Conflict,
            content = MessageResponse(message = "User already exists!"),
            json = AuthClientStubs.json
        )
        val authClient = KtorAuthClient(
            httpClient = AuthClientStubs.createHttpClient(engine)
        )
        val form = SignUpForm(
            fullName = "Chablau",
            email = "chablau@chablau.com",
            password = "chablau"
        )

        // When
        val exception = assertFailsWith(ClientRequestException::class) {
            authClient.signUp(form)
        }

        // Then
        assert(exception.response.status == HttpStatusCode.Conflict)
    }

    @Test
    fun signInShouldReturnOKWhenFormIsCorrect() = runTest {
        // Given
        val engine = mockEngineRespondingSuccessJson(
            json = AuthClientStubs.json,
            content = AuthTokenResponse(
                accessToken = "token",
                refreshToken = "token",
                expiresAt = 0L,
                type = "Bearer"
            )
        )
        val authClient = KtorAuthClient(
            httpClient = AuthClientStubs.createHttpClient(engine)
        )
        val form = SignInForm(
            username = "chablau@chablau.com",
            password = "chablau"
        )

        // When
        val authTokens = authClient.signIn(form)

        // Then
        assertEquals(expected = "token", actual = authTokens.refreshToken)
        assertEquals(expected = "token", actual = authTokens.accessToken)
        assertEquals(expected = 0, actual = authTokens.expiresAt)
        assertEquals(expected = TokenType.BEARER, actual = authTokens.type)
    }

    @Test
    fun signUpShouldReturnNotFoundWhenUserDoesntExist() = runTest {
        // Given
        val engine = mockEngineRespondingJson(
            json = AuthClientStubs.json,
            status = HttpStatusCode.NotFound,
            content = MessageResponse(message = "User not found.")
        )
        val authClient = KtorAuthClient(
            httpClient = AuthClientStubs.createHttpClient(engine)
        )
        val form = SignInForm(
            username = "chablau@chablau.com",
            password = "chablau"
        )

        // When
        val exception = assertFailsWith(ClientRequestException::class) {
            authClient.signIn(form)
        }

        // Then
        assert(exception.response.status == HttpStatusCode.NotFound)
    }

    @Test
    fun signOutShouldReturnNoContentWhenAccessTokenIsValid() = runTest {
        // Given
        val engine = mockEngineRespondingJson(
            json = AuthClientStubs.json,
            status = HttpStatusCode.NoContent,
            content = ""
        )
        val authClient = KtorAuthClient(
            httpClient = AuthClientStubs.createHttpClient(engine)
        )

        // When
        authClient.signOut()

        // Then
    }

    @Test
    fun signOutShouldReturnUnauthorizedWhenAccessTokenIsExpired() = runTest {
        // Given
        val engine = mockEngineRespondingJson(
            json = AuthClientStubs.json,
            status = HttpStatusCode.Unauthorized,
            content = MessageResponse(message = "Token expired")
        )
        val authClient = KtorAuthClient(
            httpClient = AuthClientStubs.createHttpClient(engine)
        )

        // When
        val exception = assertFailsWith(ClientRequestException::class) {
            authClient.signOut()
        }

        // Then
        assert(exception.response.status == HttpStatusCode.Unauthorized)
    }
}