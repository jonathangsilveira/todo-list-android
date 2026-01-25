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
import org.jgsilveira.todolist.android.features.auth.data.model.TokenType
import org.jgsilveira.todolist.android.features.auth.data.service.KtorAuthClient
import org.jgsilveira.todolist.android.features.auth.domain.model.SignInForm
import org.jgsilveira.todolist.android.features.auth.domain.model.SignUpForm
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
        val engine = AuthClientStubs.successResponseSignUpEngine
        val httpClient = AuthClientStubs.createKtorClient(engine)
        val apiService = KtorAuthClient(httpClient)
        val form = SignUpForm(
            fullName = "Chablau",
            email = "chablau@chablau.com",
            password = "chablau"
        )

        // When
        apiService.signUp(form)
    }

    @Test
    fun signUpShouldReturnConflictWhenEmailAlreadyExists() = runTest {
        // Given
        val engine = AuthClientStubs.conflictResponseSignUpEngine
        val httpClient = AuthClientStubs.createKtorClient(engine)
        val apiService = KtorAuthClient(httpClient)
        val form = SignUpForm(
            fullName = "Chablau",
            email = "chablau@chablau.com",
            password = "chablau"
        )

        // When
        val exception = assertFailsWith(ClientRequestException::class) {
            apiService.signUp(form)
        }

        // Then
        assert(exception.response.status == HttpStatusCode.Conflict)
    }

    @Test
    fun signInShouldReturnOKWhenFormIsCorrect() = runTest {
        // Given
        val engine = AuthClientStubs.successResponseSignInEngine
        val httpClient = AuthClientStubs.createKtorClient(engine)
        val apiService = KtorAuthClient(httpClient)
        val form = SignInForm(
            username = "chablau@chablau.com",
            password = "chablau"
        )

        // When
        val authTokens = apiService.signIn(form)

        // Then
        assertEquals(expected = "token", actual = authTokens.refreshToken)
        assertEquals(expected = "token", actual = authTokens.accessToken)
        assertEquals(expected = 0, actual = authTokens.expiresAt)
        assertEquals(expected = TokenType.BEARER, actual = authTokens.type)
    }

    @Test
    fun signUpShouldReturnNotFoundWhenUserDoesntExist() = runTest {
        // Given
        val engine = AuthClientStubs.notFoundResponseSignInEngine
        val httpClient = AuthClientStubs.createKtorClient(engine)
        val apiService = KtorAuthClient(httpClient)
        val form = SignInForm(
            username = "chablau@chablau.com",
            password = "chablau"
        )

        // When
        val exception = assertFailsWith(ClientRequestException::class) {
            apiService.signIn(form)
        }

        // Then
        assert(exception.response.status == HttpStatusCode.NotFound)
    }

    @Test
    fun signOutShouldReturnNoContentWhenAccessTokenIsValid() = runTest {
        // Given
        val engine = AuthClientStubs.noContentResponseSignOutEngine
        val httpClient = AuthClientStubs.createKtorClient(engine)
        val apiService = KtorAuthClient(httpClient)

        // When
        apiService.signOut()

        // Then
    }

    @Test
    fun signOutShouldReturnUnauthorizedWhenAccessTokenIsExpired() = runTest {
        // Given
        val engine = AuthClientStubs.unauthorizedResponseSignOutEngine
        val httpClient = AuthClientStubs.createKtorClient(engine)
        val apiService = KtorAuthClient(httpClient)

        // When
        val exception = assertFailsWith(ClientRequestException::class) {
            apiService.signOut()
        }

        // Then
        assert(exception.response.status == HttpStatusCode.Unauthorized)
    }
}